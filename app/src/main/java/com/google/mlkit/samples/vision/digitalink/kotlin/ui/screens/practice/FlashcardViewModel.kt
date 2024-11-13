package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.repo.AppRepository
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.model.FlashcardSession
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.model.FlashcardSessionItem
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.updateFlashcardLeitner
import kotlinx.coroutines.launch


class FlashcardViewModel(private val repository: AppRepository):ViewModel() {


    // MutableLiveData for lesson ID
    private val _lessonId = MutableLiveData<Long>()
    val lessonId: LiveData<Long> get() = _lessonId

    // Function to set/update the lesson ID
    fun setLessonId(newLessonId: Long) {
        _lessonId.value = newLessonId
    }

    // Function to check if lessonId is initialized
     fun isLessonIdInitialized(): Boolean {
        return _lessonId.value != null
    }



    private val _flashcards = MutableLiveData<List<Flashcard>>()
    val flashcards: LiveData<List<Flashcard>> = _flashcards

    private var flashcardSession: FlashcardSession? = null

    private val _currentFlashcardIndex = MutableLiveData(0)
    val currentFlashcardIndex: LiveData<Int> = _currentFlashcardIndex

    val currentFlashcard = MediatorLiveData<Flashcard>().apply {
        addSource(_flashcards) { startNewSession() }
        addSource(_currentFlashcardIndex) { updateCurrentFlashcard() }
    }

    // LiveData to observe all FlashcardSessionItems from the session
    private val _flashcardSessionItems = MutableLiveData<List<FlashcardSessionItem>>()
    val flashcardSessionItems: LiveData<List<FlashcardSessionItem>> = _flashcardSessionItems

//    init {
//        loadFlashcards()
//    }

     fun loadFlashcards() {
        viewModelScope.launch {
            
            if(isLessonIdInitialized()){

                //todo: null check

                val loadedFlashcards = repository.getDueFlashcardsByLesson(lessonId.value!!)
                _flashcards.value = loadedFlashcards
                startNewSession()
            }else{

                // TODO: show lesson picked does not exists
            }
            

        }
    }




    private fun startNewSession() {

        val updateFlashcard = fun(flashcard: Flashcard){
            viewModelScope.launch {
                repository.insertFlashcard(flashcard)
            }
        }

        _flashcards.value?.let { flashcardsList ->
            val sessionItems = flashcardsList.map { FlashcardSessionItem(it) }
            flashcardSession = FlashcardSession(flashcards = sessionItems, updateAction = updateFlashcard)
            _currentFlashcardIndex.value = 0
            _flashcardSessionItems.value = sessionItems // Populate session items LiveData
        }
    }

    private fun updateCurrentFlashcard() {
        flashcardSession?.let { session ->
            currentFlashcard.value = session.currentFlashcardItem()?.flashcard
        }
    }

    fun nextFlashcard() {
        flashcardSession?.nextFlashcard()?.let {
            _currentFlashcardIndex.value = flashcardSession?.currentIndex
        }
    }

    fun previousFlashcard() {
        flashcardSession?.previousFlashcard()?.let {
            _currentFlashcardIndex.value = flashcardSession?.currentIndex
        }
    }

    fun markCurrentFlashcard(isCorrect: Boolean) {
        flashcardSession?.markCurrentFlashcard(isCorrect)

        nextFlashcard()

    }

    fun endSession(): Pair<Int, Int>? {
        flashcardSession?.endSession()
        return flashcardSession?.getSummary()
    }

    // Function to get all FlashcardSessionItems
    fun getAllFlashcardSessionItems(): List<FlashcardSessionItem>? {
        return flashcardSession?.flashcards
    }

    // MutableLiveData to hold the string value
    private val _writtenText = MutableLiveData("Initial Value")
    val writtenText: LiveData<String> = _writtenText

    // Function to update the text
    fun updateWrittenText(newText: String) {
        _writtenText.value = newText
    }

    fun isWrittenWordCorrect(recognizedText: String): Boolean{

        val currentWord = currentFlashcard.value?.word


        if(currentWord?.let { areBanglaStringsEqual(it, recognizedText) } == true){

            markCurrentFlashcard(true)
            return true

        }else{

            markCurrentFlashcard(false)
            return false
        }
    }


    //Matching
    private fun areBanglaStringsEqual(str1: String, str2: String): Boolean {
        return str1 == str2
    }





}