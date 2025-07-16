package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.repo.AppRepository
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard
import kotlinx.coroutines.launch


class EditViewModel: ViewModel() {

    // Mutable state for expansion
    private var _isExpanded = mutableStateOf(false)
    val isExpanded: State<Boolean> = _isExpanded

    // Mutable state for the information message
    private var _text = mutableStateOf("Tap import to add words in bulk")
    val text: State<String> = _text

    // Toggle method to change the state
    fun toggleExpansion(isExpanded: Boolean) {
        _isExpanded.value = isExpanded

        if(!isExpanded){

            updateText("Tap import to add words in bulk")
        }else{
            updateText("Import functionality is not implemented. Use type instead")
        }
    }
    // Function to update the text
    fun updateText(newText: String) {
        _text.value = "$newText"
    }



}