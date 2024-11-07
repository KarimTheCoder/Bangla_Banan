package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.activity.MainActivity
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.PracticeTopBar
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.MyXmlButtonView
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.StrokeManager
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppRepository
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.FlashcardViewModelFactory
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.MyAppDatabase
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.flashcard.FlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(navController: NavController, mainActivity: MainActivity) {

    // Obtain an instance of FlashcardViewModel
    val database = MyAppDatabase.getDatabase(mainActivity)
    val repository = AppRepository(database.folderDao())
    val cardViewModel =  FlashcardViewModel(repository)

    var viewModel = PracticeViewModel()
    viewModel.initializeDrawingRecognition()
    val strokeManager = viewModel.strokeManager
    strokeManager.setClearCurrentInkAfterRecognition(true)
    strokeManager.setTriggerRecognitionAfterInput(false)




    val listener = object : StrokeManager.StatusChangedListener {
        override fun onStatusChanged() {
            strokeManager.text?.let { viewModel.updateText(it) }
            Log.i("regtag", "status changed: ${strokeManager.text}")
        }
    }
    strokeManager.setStatusChangedListener(listener)

    Column {

        PracticeTopBar(navController)
        TwoBoxesWithLines0(viewModel)
        HorizontalLayoutWithTextButtonAndMatchButton(strokeManager, viewModel)
        MyXmlButtonView(strokeManager,viewModel)


        Button(onClick = {
                        viewModel.toggleCorrect()
            }) {

            Text(text = "Correct")

        }
        Button(onClick = {
            viewModel.toggleWrong()
        }) {

            Text(text = "Wrong")

        }


    }



  //  FlashcardScreen(mainActivity)


}    @Composable
fun FlashcardScreen(mainActivity: MainActivity) {

    // Obtain an instance of FlashcardViewModel
    val database = MyAppDatabase.getDatabase(mainActivity)
    val repository = AppRepository(database.folderDao())
    val viewModel =  FlashcardViewModel(repository)


    FlashcardViewer(viewModel = viewModel)


}