package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val repository: FlashcardRepository)  : ViewModel() {

    fun insertFlashcard(flashcard: Flashcard) = viewModelScope.launch {
        repository.insertFlashcard(flashcard)
    }

    fun updateFlashcard(flashcard: Flashcard) = viewModelScope.launch {
        repository.updateFlashcard(flashcard)
    }

    fun deleteFlashcard(flashcard: Flashcard) = viewModelScope.launch {
        repository.deleteFlashcard(flashcard)
    }

    fun getDueFlashcards(currentTime: Long): LiveData<List<Flashcard>> = liveData {
        emit(repository.getDueFlashcards(currentTime))
    }

    fun getFlashcard(word: String): LiveData<Flashcard?> = liveData {
        emit(repository.getFlashcard(word))
    }

    // This is the LiveData that other classes can observe
    val allFlashcards: LiveData<List<Flashcard>> = liveData {
        emit(repository.getAllFlashcards())
    }
}