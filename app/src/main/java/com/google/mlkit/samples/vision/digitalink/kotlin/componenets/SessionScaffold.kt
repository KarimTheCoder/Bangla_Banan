package com.google.mlkit.samples.vision.digitalink.kotlin.componenets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope

// Function to create TopAppBar and Scaffold
@Composable
fun MyScaffold(drawerState: DrawerState, scope: CoroutineScope) {
    Scaffold(
        topBar = { SessionTopBar(drawerState, scope) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            SegmentedButton()
            TitleListDemo()

        }

    }
}
