package com.google.mlkit.samples.vision.digitalink.kotlin.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.activity.MainActivity
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.repo.AppRepository
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.MyAppDatabase
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete.CompleteScreen
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.edit.EditScreen
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.LessonScreen
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.screen.PracticeScreen


@Composable
fun AppNavigation(viewModel: AppDatabaseViewModel, mainActivity: MainActivity) {

    val database = MyAppDatabase.getDatabase(mainActivity)
    val repository = AppRepository(database.folderDao())
    val flashcardVM = FlashcardViewModel(repository)

    val navController = rememberNavController()

    val context = LocalContext.current
    val versionName = try {
        context.packageManager
            .getPackageInfo(context.packageName, 0).versionName
    } catch (e: Exception) {
        "Unknown"
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Prototype - v$versionName",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home_screen",
            modifier = Modifier.padding(paddingValues) // Prevents overlap with bottomBar
        ) {
            composable("home_screen") {
                LessonScreen(navController, viewModel, flashcardVM)
            }
            composable("edit_screen") {
                EditScreen(navController, viewModel)
            }
            composable("practice_screen") {
                PracticeScreen(navController, flashcardVM)
            }
            composable("complete_screen") {
                CompleteScreen(navController, viewModel, flashcardVM)
            }
        }
    }
}


























