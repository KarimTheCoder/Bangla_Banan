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
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.repo.AppRepository
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.FlashcardViewModelFactory
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Folder
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Lesson
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.MyAppDatabase
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.navigation.AppNavigation
import com.google.mlkit.samples.vision.digitalink.ui.theme.MLKitDigitalInkRecognitionDemoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
//            flashcardViewModel.fetchAllLessons()
//            flashcardViewModel.fetchAllFlashcards()

            MLKitDigitalInkRecognitionDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                ) { innerPadding ->


                    AppNavigation(flashcardViewModel,this)



                    //InsertSampleData(viewModel = flashcardViewModel)

                }
            }
        }
    }







}

@Composable
fun InsertSampleData(viewModel: AppDatabaseViewModel) {

    // Define sample folder list
    val folderList = listOf(
        Folder(folderName = "বাংলাদেশ", folderId = Random.nextLong()),
        Folder(folderName = "Mathematics", folderId = Random.nextLong())
    )

    // Define sample lesson list, with each lesson linked to its folder by folderId
    val lessonList = folderList.flatMap { folder ->
        listOf(
            Lesson(lessonName = "ফল", folderOwnerId = folder.folderId, lessonId = Random.nextLong()),
            Lesson(lessonName = "ফুল", folderOwnerId = folder.folderId, lessonId = Random.nextLong()),
            Lesson(lessonName = "সবজি", folderOwnerId = folder.folderId, lessonId = Random.nextLong())
        )
    }

    // Define sample flashcard list, with each flashcard linked to its lesson by lessonId
    val flashcardList = lessonList.flatMap { lesson ->
        listOf(
            Flashcard(word = "কলা", definition = "Basic unit of life.", lessonOwnerId = lesson.lessonId),
            Flashcard(word = "আম", definition = "Smallest unit of matter.", lessonOwnerId = lesson.lessonId),
//            Flashcard(word = "কমলা", definition = "Interaction that changes motion.", lessonOwnerId = lesson.lessonId),
//            Flashcard(word = "লিচু", definition = "Capacity to do work.", lessonOwnerId = lesson.lessonId),
//            Flashcard(word = "তরমুজ", definition = "Combination of atoms.", lessonOwnerId = lesson.lessonId)
        )
    }

    // Button to insert data
    Button(onClick = {
        // Insert folders
        folderList.forEach { folder ->
            viewModel.insertFolder(folder)
        }

        // Insert lessons
        lessonList.forEach { lesson ->
            viewModel.insertLesson(lesson, lesson.folderOwnerId)
        }

        // Insert flashcards
        flashcardList.forEach { flashcard ->
            viewModel.insertFlashcard(flashcard)
        }
    }) {
        Text("Insert Sample Data")
    }
}