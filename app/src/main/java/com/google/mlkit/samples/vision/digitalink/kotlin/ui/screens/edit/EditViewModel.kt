package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class EditViewModel: ViewModel() {

    // Mutable state for expansion (Type vs Import toggle)
    private var _isExpanded = mutableStateOf(false)
    val isExpanded: State<Boolean> = _isExpanded

    // Mutable state for the information message
    private var _text = mutableStateOf("Tap import to add words in bulk")
    val text: State<String> = _text

    // Import result state
    private var _importResult = mutableStateOf("")
    val importResult: State<String> = _importResult

    // Toggle method to change the state
    fun toggleExpansion(isExpanded: Boolean) {
        _isExpanded.value = isExpanded

        if(!isExpanded){
            updateText("Tap import to add words in bulk")
            _importResult.value = ""
        }else{
            updateText("Paste words below, one per line")
            _importResult.value = ""
        }
    }

    // Function to update the text
    fun updateText(newText: String) {
        _text.value = newText
    }

    // Function to set import result
    fun setImportResult(imported: Int, skipped: Int) {
        _importResult.value = if (skipped > 0) {
            "✓ $imported words imported, $skipped non-Bangla skipped"
        } else {
            "✓ $imported words imported"
        }
    }
}