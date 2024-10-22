package com.google.mlkit.samples.vision.digitalink.kotlin.componenets.session

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope

// Function to create TopAppBar and Scaffold
@Composable
fun SessionScaffold(drawerState: DrawerState, scope: CoroutineScope) {
    Scaffold(
        topBar = { SessionTopBar(drawerState, scope) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            SegmentedButton()
            TitleListDemo()

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
            TitleListDemo()

        }

    }
}
