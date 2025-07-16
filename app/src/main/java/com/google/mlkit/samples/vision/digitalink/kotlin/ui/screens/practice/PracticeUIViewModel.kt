package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.DrawingView
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.StrokeManager
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.flashcard.CardState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PracticeUIViewModel: ViewModel() {

    val BANGLA_LANG_CODE: String = "bn"
    val strokeManager = StrokeManager()

    // MutableLiveData to hold the string value
    private val _text = MutableLiveData("Initial Value")
    val statusText: LiveData<String> = _text

    private val _status = MutableStateFlow("Download started...")
    val status: StateFlow<String> = _status

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Function to update the text
    fun updateStatusText(newText: String) {
        _text.value = newText
    }


    // MutableState for DrawingView
    val drawingViewRef = mutableStateOf<DrawingView?>(null)



    fun initializeDrawingRecognition() {
        strokeManager.setActiveModel(BANGLA_LANG_CODE)
        strokeManager.download()
        strokeManager.reset()

        strokeManager.setDownloadStatusListener(object : StrokeManager.DownloadStatusListener {
            override fun onDownloadStarted() {
                _status.value = "Download started..."
                _isLoading.value = true
            }

            override fun onDownloadSucceeded() {
                _status.value = "Ready!"
                _text.value = "Ready!"
                _isLoading.value = false
            }

            override fun onDownloadFailed(error: String) {
                _status.value = "Download failed: check internet!"
                _isLoading.value = false
            }
        })
    }

    fun recognizeClick(): Task<String?> {
        return strokeManager.recognize()
    }

    // Mutable background color state
    private val _backgroundColor = MutableStateFlow(Color.LightGray)
    val backgroundColor: StateFlow<Color> = _backgroundColor


    // Hold visibility state
    private val _isVisible = MutableStateFlow(true)
    val isVisible: StateFlow<Boolean> = _isVisible


    // Toggle function for visibility
    fun toggleCorrect() {
        viewModelScope.launch {
            _isVisible.value = !_isVisible.value

            if (_isVisible.value == false) {
                _backgroundColor.value = Color.Green
                setCardState(CardState.CORRECT)
                delay(1200) // Wait for 300 milliseconds
                _backgroundColor.value = Color.LightGray
                toggleCorrect()
                clearScreen()
            }
        }
    }
    // Toggle function for visibility
    fun toggleWrong() {
        viewModelScope.launch {
            _isVisible.value = !_isVisible.value
            if (_isVisible.value == false) {
                _backgroundColor.value = Color.Red
                setCardState(CardState.WRONG)
                delay(1200) // Wait for 300 milliseconds
                _backgroundColor.value = Color.LightGray
                toggleWrong()

                clearScreen()

            }
        }
    }

    private fun clearScreen(){

        strokeManager.reset()
        drawingViewRef.value?.clear()
        updateStatusText("")

    }



    private val _cardState = MutableLiveData(CardState.SHOW)
    val cardState: LiveData<CardState> = _cardState


    private fun setCardState(cardState: CardState){

        _cardState.value = cardState
    }



}