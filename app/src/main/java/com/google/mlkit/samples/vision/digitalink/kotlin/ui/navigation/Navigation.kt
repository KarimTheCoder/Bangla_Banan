package com.google.mlkit.samples.vision.digitalink.kotlin.ui.navigation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.draw.MyXmlButtonView
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.draw.StrokeManager
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.edit.TypeInput
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.lesson.EditSegmentedButton
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.lesson.EditTopBar
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.lesson.MyDrawerLayout
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.lesson.MainScaffold
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.lesson.PracticeTopBar
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.lesson.SessionCompletedScaffold
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.practice.HorizontalLayoutWithTextButtonAndMatchButton
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.practice.PracticeViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.practice.TwoBoxesWithLines


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home_screen")
    {
        composable("home_screen") {
            HomeScreen(navController)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {


    MyDrawerLayout { drawerState, scope ->
        MainScaffold(navController, drawerState, scope)
    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(navController: NavController) {

    Column {
        EditTopBar(navController)
        EditSegmentedButton()
        TypeInput()
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(navController: NavController) {


    var viewModel: PracticeViewModel = PracticeViewModel()
    viewModel.initializeDrawingRecognition()
    val strokeManager = viewModel.strokeManager
    //strokeManager.setStatusChangedListener()
    strokeManager.setClearCurrentInkAfterRecognition(true)
    strokeManager.setTriggerRecognitionAfterInput(false)




    val listener = object : StrokeManager.StatusChangedListener {
        override fun onStatusChanged() {
            //val strokeManager = viewModel.strokeManager // Assuming you have access to the StrokeManager

            strokeManager.text?.let { viewModel.updateText(it) }
            Log.i("regtag", "status changed: ${strokeManager.text}")
        }
    }
    strokeManager.setStatusChangedListener(listener)

    Column {

        PracticeTopBar(navController)
        TwoBoxesWithLines()
        HorizontalLayoutWithTextButtonAndMatchButton(strokeManager, viewModel)

        MyXmlButtonView(strokeManager,viewModel)


    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteScreen(navController: NavController) {

    MyDrawerLayout { drawerState, scope ->
        SessionCompletedScaffold( navController, drawerState, scope)
    }

}




















