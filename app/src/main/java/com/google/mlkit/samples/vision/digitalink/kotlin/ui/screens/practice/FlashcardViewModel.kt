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
import java.text.Normalizer


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
        updateSessionProgress()
        viewModelScope.launch {
            delay(1400) // Delay for 1200ms
            triggerIconClick()
        }
    }

    fun previousFlashcard() {
        flashcardSession?.previousFlashcard()?.let {
            _currentFlashcardIndex.value = flashcardSession?.currentIndex
        }
        updateSessionProgress()
        viewModelScope.launch {
            delay(1400) // Delay for 1200ms
            triggerIconClick()
        }
    }

    fun markCurrentFlashcard(isCorrect: Boolean) {

        if(flashcardSession?.isSessionComplete() == false){
            flashcardSession?.markCurrentFlashcard(isCorrect)
            updateSessionProgress()
            nextFlashcard()

        }else{
            Log.i("FlashcardSession","Session completed")
            endSession()
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
    private val _statusText = MutableLiveData("Initial Value")
    val statusText: LiveData<String> = _statusText

    // Function to update the text
    fun updateStatusText(newText: String) {
        _statusText.value = newText
    }

    private val _writtenText = MutableLiveData("No value")
    val writtenText: LiveData<String> = _writtenText

    // Function to update the text
    fun updateWrittenText(newText: String) {
        _writtenText.value = newText
    }
    private val _currentWord = MutableLiveData("No value")
    val currentWord: LiveData<String> = _currentWord

    // Function to update the text
    fun updateCurrentWord(newText: String) {
        _currentWord.value = newText
    }



    fun isWrittenWordCorrect(recognizedText: String): Boolean{

        val currentWord = currentFlashcard.value?.word
        updateCurrentWord(currentWord.toString())
        updateWrittenText(recognizedText)

        if(currentWord?.let { areBanglaStringsEqual(it, recognizedText) } == true){

            markCurrentFlashcard(true)
            return true

        }else{

            markCurrentFlashcard(false)

            return false
        }
    }

    private fun areBanglaStringsEqual(str1: String, str2: String): Boolean {
        // Normalize the strings to remove any Unicode discrepancies
        val normalizedStr1 = Normalizer.normalize(str1.trim(), Normalizer.Form.NFC)
        val normalizedStr2 = Normalizer.normalize(str2.trim(), Normalizer.Form.NFC)

        // Compare the normalized strings
        return normalizedStr1 == normalizedStr2
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



    // Progress
    // MutableStateFlow to track session progress
    private val _sessionProgress = MutableStateFlow(0f)
    val sessionProgress: StateFlow<Float> = _sessionProgress

    private fun calculateSessionProgress(): Float {
        val totalMaxReviews = flashcardSession?.flashcards?.sumOf { it.maxReviewCount } ?: 0
        if (totalMaxReviews == 0) return 0f // Avoid division by zero

        val totalReviewsDone = flashcardSession?.flashcards?.sumOf { it.reviewCount } ?: 0
        return (totalReviewsDone.toFloat() / totalMaxReviews)
    }

    fun updateSessionProgress() {
        val progress = calculateSessionProgress()
        _sessionProgress.value = progress

        // Trigger endFunction if progress reaches 100%
        if (progress >= 100f) {
            endSession()
        }
    }


}