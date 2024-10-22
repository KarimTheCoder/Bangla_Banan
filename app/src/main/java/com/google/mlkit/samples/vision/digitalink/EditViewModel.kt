package com.google.mlkit.samples.vision.digitalink

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap


class EditViewModel: ViewModel() {

    // Mutable state for expansion
    private var _isExpanded = mutableStateOf(false)
    val isExpanded: State<Boolean> = _isExpanded

    // Mutable state for the information message
    private var _text = mutableStateOf("This is an information message.")
    val text: State<String> = _text

    // Toggle method to change the state
    fun toggleExpansion(isExpanded: Boolean) {
        _isExpanded.value = isExpanded

        if(!isExpanded){

            updateText("This is an information message.")
        }else{
            updateText("This is another information message.")
        }
    }

    // Function to update the text
    fun updateText(newText: String) {
        _text.value = newText
    }

}