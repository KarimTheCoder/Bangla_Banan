package com.google.mlkit.samples.vision.digitalink.kotlin.ui.complete

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.lesson.MyDrawerLayout
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.lesson.SessionCompletedScaffold
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.list.DemoNormalList
import com.google.mlkit.samples.vision.digitalink.ui.theme.MLKitDigitalInkRecognitionDemoTheme

class SessionCompleteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MLKitDigitalInkRecognitionDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->



//                        MyDrawerLayout { drawerState, scope ->
//                            SessionCompletedScaffold(this, drawerState, scope)
//                        }
//
//





                }
            }
        }
    }
}

@Composable
fun ToggleSegmentedButton(navController: NavController) {
    // Boolean state for visibility
    // Boolean state for visibility

    val viewModel: SessionCompleteViewModel = viewModel()
    val isLessonVisible by viewModel.isLessonVisible.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {


    // Animate appearance and disappearance of TitleListDemo sliding from the right
        AnimatedVisibility(
            visible = isLessonVisible,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),  // Slide in from the right
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()   // Slide out to the right
        ) {
            DemoNormalList(navController)
        }

        // Animate appearance and disappearance of ScrollableListWithFixedButtons sliding from the right
        AnimatedVisibility(
            visible = !isLessonVisible,
            enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),  // Slide in from the right
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()   // Slide out to the right
        ) {
            ResultUI(
                items = List(15) { "Item $it" },
                onButton1Click = { /* Do something */ },
                onButton2Click = { /* Do something */ }
            )
        }
    }
}