package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.MyDrawerLayout
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.SessionCompletedScaffold

@Composable
fun CompleteScreen(navController: NavController) {

    MyDrawerLayout { drawerState, scope ->
        SessionCompletedScaffold( navController, drawerState, scope)
    }

}
