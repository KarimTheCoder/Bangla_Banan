package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.repo.AppRepository
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.model.FlashcardSession
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.model.FlashcardSessionItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

                val loadedFlashcards = repository.getBalancedFlashcards(10,lessonId.value!!)

                Log.i("FlashcardSession","${loadedFlashcards.size} flashcards added")
                _flashcards.value = loadedFlashcards
                startNewSession()
            }else{

                // TODO: show lesson picked does not exists
            }
            

        }
    }

    private fun startNewSession() {
        _flashcards.value?.let { flashcardsList ->
            val sessionItems = flashcardsList.map { FlashcardSessionItem(it) }

            // Pass the existing `updateFlashcard` function reference
            flashcardSession = FlashcardSession(flashcards = sessionItems, updateAction = ::updateFlashcard, updateFlashcardLeitner =  ::updateFlashcardLeitner)

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
        viewModelScope.launch {
            delay(1400) // Delay for 1200ms
            triggerIconClick()
        }
    }

    fun previousFlashcard() {
        flashcardSession?.previousFlashcard()?.let {
            _currentFlashcardIndex.value = flashcardSession?.currentIndex
        }
        viewModelScope.launch {
            delay(1400) // Delay for 1200ms
            triggerIconClick()
        }
    }

    fun markCurrentFlashcard(isCorrect: Boolean) {

        if(flashcardSession?.isSessionComplete() == false){
            flashcardSession?.markCurrentFlashcard(isCorrect)
            nextFlashcard()

        }else{
            Log.i("FlashcardSession","Session completed")
        }
    }



    fun endSession() {
        flashcardSession?.endSession()

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



    fun updateFlashcard(flashcard: Flashcard) {
        viewModelScope.launch {
            repository.updateFlashcard(flashcard)
        }
    }

    fun updateFlashcardLeitner(flashcard: Flashcard, isCorrect: Boolean) {
        viewModelScope.launch {
            repository.updateFlashcardLeitner(flashcard,isCorrect)
        }
    }


    // StateFlow to hold familiarity progress
    private val _familiarityProgress = MutableStateFlow<List<Boolean>>(emptyList())
    val familiarityProgress: StateFlow<List<Boolean>> = _familiarityProgress

    // Function to calculate familiarity progress and update the StateFlow
    fun fetchFamiliarityProgress(flashcardId: Long) {
        viewModelScope.launch {
            try {
                val progress = repository.calculateFamiliarityProgress(flashcardId)
                _familiarityProgress.value = progress
            } catch (e: Exception) {
                // Handle exceptions, e.g., log the error or notify the user
                _familiarityProgress.value = emptyList() // Fallback
            }
        }
    }

    // StateFlow to hold familiarity progress
    private val _spacedRepetitionProgress = MutableStateFlow<List<Boolean>>(emptyList())
    val spacedRepetitionProgress: StateFlow<List<Boolean>> = _spacedRepetitionProgress

    // Function to calculate familiarity progress and update the StateFlow
    fun fetchSpacedRepetitionProgress(flashcardId: Long) {
        viewModelScope.launch {
            try {
                val progress = repository.calculateSpacedRepetitionProgress(flashcardId)
                _spacedRepetitionProgress.value = progress
            } catch (e: Exception) {
                // Handle exceptions, e.g., log the error or notify the user
                _spacedRepetitionProgress.value = emptyList() // Fallback
            }
        }
    }




        private val _onIconClick = MutableStateFlow(false)
        val onIconClick: StateFlow<Boolean> = _onIconClick

        fun triggerIconClick() {
            _onIconClick.value = true
        }

        fun resetIconClick() {
            _onIconClick.value = false
        }


}