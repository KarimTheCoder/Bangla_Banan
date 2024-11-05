package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.MainScaffold
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.MyDrawerLayout
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.FlashcardViewModel


@Composable
fun LessonScreen(navController: NavController, viewModel: FlashcardViewModel){



    MyDrawerLayout { drawerState, scope ->
        MainScaffold(navController, drawerState, scope,viewModel)
    }

}