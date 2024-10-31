package com.google.mlkit.samples.vision.digitalink.kotlin.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.MyXmlButtonView
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.draw.StrokeManager
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.edit.TypeInput
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.EditSegmentedButton
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.EditTopBar
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.MyDrawerLayout
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.MainScaffold
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.PracticeTopBar
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.SessionCompletedScaffold
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete.CompleteScreen
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.edit.EditScreen
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.LessonScreen
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.HorizontalLayoutWithTextButtonAndMatchButton
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.PracticeScreen
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.PracticeViewModel



@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home_screen")
    {
        composable("home_screen") {
            LessonScreen(navController)
        }
        composable("edit_screen") {
            EditScreen(navController)

        }

        composable("practice_screen") {
            PracticeScreen( navController)

        }
        composable("complete_screen") {
            CompleteScreen(navController)

        }
    }
}



























