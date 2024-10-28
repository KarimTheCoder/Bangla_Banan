package com.google.mlkit.samples.vision.digitalink.kotlin.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    // StateFlow to hold practice or Edit state

    private val _isPractice = MutableStateFlow(true)
    val isPractice: StateFlow<Boolean> = _isPractice

    // Toggle visibility
    fun toggleIsPractice(isPractice : Boolean) {
        viewModelScope.launch {
            _isPractice.value = isPractice
        }
    }


}