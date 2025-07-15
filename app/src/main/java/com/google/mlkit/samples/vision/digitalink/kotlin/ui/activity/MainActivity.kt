package com.google.mlkit.samples.vision.digitalink.kotlin.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.repo.AppRepository
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.FlashcardViewModelFactory
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.loadFolderFromJsonData
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.loadJsonData
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.loadLessonsFromJsonData
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.printJsonFlashcards
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.printJsonFolder
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.printJsonLessons
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Folder
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Lesson
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.MyAppDatabase
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.navigation.AppNavigation
import com.google.mlkit.samples.vision.digitalink.ui.theme.MLKitDigitalInkRecognitionDemoTheme
import com.samsulkarim.bananapp.R
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
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


                    printJsonFolder(this)
                    printJsonLessons(this)
                    printJsonFlashcards(this)

                    //InsertSampleData(viewModel = flashcardViewModel,this)

                }
            }
        }
    }







}

@Composable
fun InsertSampleData(viewModel: AppDatabaseViewModel, context: Context) {

    // Define sample folder list
    val folderList = loadFolderFromJsonData(context)

    // Define sample lesson list, with each lesson linked to its folder by folderId
    val lessonList = loadLessonsFromJsonData(context)
    // Define sample flashcard list, with each flashcard linked to its lesson by lessonId
    val flashcardList = loadJsonData(context)

    // Button to insert data
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Button(
        onClick = {
            folderList.forEach { folder ->
                viewModel.insertFolder(folder)
            }
            lessonList.forEach { lesson ->
                viewModel.insertLesson(lesson, lesson.folderOwnerId)
            }
            flashcardList.forEach { flashcard ->
                viewModel.insertFlashcard(flashcard)
            }
        },
        modifier = Modifier
            .scale(scale),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary) // Vibrant Orange
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_create_new_folder_24),
            contentDescription = "Insert",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text("Insert Sample Data", color = Color.White)
    }
}

//@Composable
//fun InsertJsonData(viewModel: AppDatabaseViewModel, context: Context) {
//    // Load JSON data
//    val jsonData = loadJsonData(context)
//
//    // Button to insert data
//    Button(onClick = {
//        // Group data into Folders, Lessons, and Flashcards
//        val folderMap = mutableMapOf<String, Long>() // folderName -> folderId
//        val lessonMap = mutableMapOf<Pair<String, String>, Long>() // (folderName, lessonName) -> lessonId
//
//        jsonData.forEach { data ->
//
//            val folderName = data.folder.takeIf { it.isNotBlank() } ?: "Unknown"
//            val lessonName = data.Lesson
//
//            // Insert folder if not already inserted
//            if (folderName !in folderMap) {
//                val folderId = Random.nextLong()
//                folderMap[folderName] = folderId
//                viewModel.insertFolder(Folder(folderId = folderId, folderName = folderName))
//            }
//
//            // Insert lesson if not already inserted
//            val folderId = folderMap[folderName]!!
//            if (Pair(folderName, lessonName) !in lessonMap) {
//                val lessonId = Random.nextLong()
//                lessonMap[Pair(folderName, lessonName)] = lessonId
//                viewModel.insertLesson(Lesson(lessonId = lessonId, lessonName = lessonName, folderOwnerId = folderId), folderId)
//            }
//
//            // Insert flashcard
//            val lessonId = lessonMap[Pair(folderName, lessonName)]!!
//            viewModel.insertFlashcard(Flashcard(word = data.word, definition = data.definition, lessonOwnerId = lessonId))
//        }
//    }) {
//        Text("Insert JSON Data")
//    }
//}