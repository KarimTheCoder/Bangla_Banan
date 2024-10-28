package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.DrawingView
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.StrokeManager

class PracticeViewModel: ViewModel() {

    val BANGLA_LANG_CODE: String = "bn"
    val strokeManager = StrokeManager()

    // MutableLiveData to hold the string value
    private val _text = MutableLiveData("Initial Value")
    val text: LiveData<String> = _text

    // Function to update the text
    fun updateText(newText: String) {
        _text.value = newText
    }


    // MutableState for DrawingView
    val drawingViewRef = mutableStateOf<DrawingView?>(null)



    fun initializeDrawingRecognition(){

        strokeManager.setActiveModel(BANGLA_LANG_CODE) // Sets recognized language
        strokeManager.download()
        strokeManager.reset()
    }

    fun recognizeClick() {
        strokeManager.recognize()
    }






}