package com.google.mlkit.samples.vision.digitalink.kotlin.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete.ToggleSegmentedButton
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.list.DemoNormalList
import kotlinx.coroutines.CoroutineScope

// Function to create TopAppBar and Scaffold
@Composable
fun MainScaffold(navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    Scaffold(
        topBar = { SessionTopBar(drawerState, scope) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            SegmentedButton()
            DemoNormalList(navController)

        }

    }
}

// Function to create TopAppBar and Scaffold
@Composable
fun SessionCompletedScaffold(navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    Scaffold(
        topBar = { SessionCompleteTopBar(navController, drawerState, scope) }
    ) { paddingValues ->


        Column(modifier = Modifier.padding(paddingValues)) {

            SessionCompletedSegmentedButton()
            ToggleSegmentedButton(navController)
        }

    }
}

@Composable
fun EditScaffold(navController: NavController) {
    Scaffold(
        topBar = { EditTopBar(navController) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            SegmentedButton()
            DemoNormalList(navController)

        }

    }
}
