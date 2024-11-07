package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.MainScaffold
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.MyDrawerLayout
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel


@Composable
fun LessonScreen(navController: NavController, viewModel: AppDatabaseViewModel){



    MyDrawerLayout (viewModel){ drawerState, scope ->
        MainScaffold(navController, drawerState, scope,viewModel)
    }

}