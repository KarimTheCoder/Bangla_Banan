package com.google.mlkit.samples.vision.digitalink.kotlin.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppRepository
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.Flashcard
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.FlashcardViewModelFactory
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.Folder
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.Lesson
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.MyAppDatabase
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.navigation.AppNavigation
import com.google.mlkit.samples.vision.digitalink.ui.theme.MLKitDigitalInkRecognitionDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //private val viewModel: FlashcardViewModel by viewModels()








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val flashcardViewModel: AppDatabaseViewModel

            val database = MyAppDatabase.getDatabase(this)
            val repository = AppRepository(database.folderDao())
            val viewModelFactory = FlashcardViewModelFactory(repository)
            flashcardViewModel = ViewModelProvider(
                this,
                viewModelFactory
            )[AppDatabaseViewModel::class.java]

            flashcardViewModel.fetchAllFolders()
            flashcardViewModel.fetchAllLessons()
            flashcardViewModel.fetchAllFlashcards()

            MLKitDigitalInkRecognitionDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                ) { innerPadding ->


                    AppNavigation(flashcardViewModel,this)



                   // InsertSampleData(viewModel = flashcardViewModel)

                }
            }
        }
    }







}

@Composable
fun InsertSampleData(viewModel: AppDatabaseViewModel) {
    Button(onClick = {
        val folder = Folder(folderName = "Science")
        val lesson = Lesson(lessonName = "Biology Basics", folderOwnerId = 1)
        val flashcard = Flashcard(word = "Cell", definition = "Basic unit of life.", lessonOwnerId = 1)

        viewModel.insertFolder(folder)
        viewModel.insertLesson(lesson)
        viewModel.insertFlashcard(flashcard)
    }) {
        Text("Insert Sample Data")
    }
}
