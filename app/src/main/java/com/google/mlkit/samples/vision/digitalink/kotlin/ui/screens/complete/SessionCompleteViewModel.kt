package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionCompleteViewModel: ViewModel() {

    // State to track visibility
    private val _isLessonVisible = MutableStateFlow(false)
    val isLessonVisible: StateFlow<Boolean> = _isLessonVisible

    // Function to toggle visibility
    fun toggleVisibility(isLessonVisible: Boolean) {
        _isLessonVisible.value = isLessonVisible
    }


}