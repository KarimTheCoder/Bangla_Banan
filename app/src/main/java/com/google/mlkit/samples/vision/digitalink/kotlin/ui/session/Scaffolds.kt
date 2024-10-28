package com.google.mlkit.samples.vision.digitalink.kotlin.ui.session

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.complete.ToggleSegmentedButton
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.list.DemoNormalList
import kotlinx.coroutines.CoroutineScope

// Function to create TopAppBar and Scaffold
@Composable
fun SessionScaffold(drawerState: DrawerState, scope: CoroutineScope) {
    Scaffold(
        topBar = { SessionTopBar(drawerState, scope) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            SegmentedButton()
            DemoNormalList()

        }

    }
}

// Function to create TopAppBar and Scaffold
@Composable
fun SessionCompletedScaffold(drawerState: DrawerState, scope: CoroutineScope) {
    Scaffold(
        topBar = { SessionCompleteTopBar(drawerState, scope) }
    ) { paddingValues ->


        Column(modifier = Modifier.padding(paddingValues)) {

            SessionCompletedSegmentedButton()
            ToggleSegmentedButton()
        }

    }
}

@Composable
fun EditScaffold() {
    Scaffold(
        topBar = { EditTopBar() }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            SegmentedButton()
            DemoNormalList()

        }

    }
}
