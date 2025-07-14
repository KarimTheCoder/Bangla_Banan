package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.components.HomeDrawer
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.components.HomeScaffold
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel


@Composable
fun LessonScreen(
    navController: NavController,
    viewModel: AppDatabaseViewModel,
    flashcardVM: FlashcardViewModel
){

    HomeDrawer (viewModel){ drawerState, scope ->
        HomeScaffold(navController, drawerState, scope,viewModel,flashcardVM)
    }

}