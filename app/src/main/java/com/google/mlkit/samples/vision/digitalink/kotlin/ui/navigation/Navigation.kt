package com.google.mlkit.samples.vision.digitalink.kotlin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.FlashcardViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete.CompleteScreen
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.edit.EditScreen
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.LessonScreen
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.PracticeScreen


@Composable
fun AppNavigation(viewModel: FlashcardViewModel) {
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
            PracticeScreen( navController)

        }
        composable("complete_screen") {
            CompleteScreen(navController)

        }
    }
}



























