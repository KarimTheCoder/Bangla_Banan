package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.flashcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppRepository
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.Flashcard
import kotlinx.coroutines.launch


class FlashcardViewModel(private val repository: AppRepository):ViewModel() {

    private val _currentFlashcardIndex = MutableLiveData(0)
    val currentFlashcardIndex: LiveData<Int> = _currentFlashcardIndex

    private val _flashcards = MutableLiveData<List<Flashcard>>()
    val flashcards: LiveData<List<Flashcard>> = _flashcards

    val currentFlashcard = MediatorLiveData<Flashcard>().apply {
        addSource(_flashcards) { updateCurrentFlashcard() }
        addSource(_currentFlashcardIndex) { updateCurrentFlashcard() }
    }
    init {
        loadFlashcards()
    }

    private fun loadFlashcards() {
        viewModelScope.launch {
            _flashcards.value = repository.getDueFlashcards()
        }
    }

    private fun updateCurrentFlashcard() {
        val index = _currentFlashcardIndex.value ?: 0
        currentFlashcard.value = _flashcards.value?.getOrNull(index)
    }

    fun nextFlashcard() {
        _currentFlashcardIndex.value = (_currentFlashcardIndex.value?.plus(1) ?: 1) % (_flashcards.value?.size ?: 1)
    }

    fun previousFlashcard() {
        _currentFlashcardIndex.value =
            if (_currentFlashcardIndex.value == 0) (_flashcards.value?.size ?: 1) - 1
            else (_currentFlashcardIndex.value?.minus(1) ?: 0)
    }



}