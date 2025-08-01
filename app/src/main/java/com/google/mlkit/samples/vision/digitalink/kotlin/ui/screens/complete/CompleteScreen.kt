package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.SessionCompletedScaffold
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.components.HomeDrawer
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel

@Composable
fun CompleteScreen(
    navController: NavController,
    viewModel: AppDatabaseViewModel,
    flashcardVM: FlashcardViewModel
) {



    HomeDrawer(viewModel) { drawerState, scope ->

        SessionCompletedScaffold( navController, drawerState, scope,flashcardVM)
    }

}
