package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FlashcardViewModelFactory (private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppFlashcardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppFlashcardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}