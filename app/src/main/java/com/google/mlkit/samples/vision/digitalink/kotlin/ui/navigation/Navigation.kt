package com.google.mlkit.samples.vision.digitalink.kotlin.ui.navigation

import androidx.compose.runtime.Composable
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

    // Obtain an instance of FlashcardViewModel
    val database = MyAppDatabase.getDatabase(mainActivity)
    val repository = AppRepository(database.folderDao())
    val flashcardVM =  FlashcardViewModel(repository)


    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home_screen")
    {
        composable("home_screen") {
            LessonScreen(navController,viewModel)
        }
        composable("edit_screen") {
            EditScreen(navController,viewModel)

        }

        composable("practice_screen") {
            PracticeScreen( navController,flashcardVM)

        }
        composable("complete_screen") {
            CompleteScreen(navController,viewModel,flashcardVM)

        }
    }
}



























