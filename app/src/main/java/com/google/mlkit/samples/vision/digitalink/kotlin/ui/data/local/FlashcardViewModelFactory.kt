package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.repo.AppRepository

class FlashcardViewModelFactory (private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppDatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppDatabaseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}