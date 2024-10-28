package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.EditSegmentedButton
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.EditTopBar

@Composable
fun EditScreen(navController: NavController) {

    Column {
        EditTopBar(navController)
        EditSegmentedButton()
        TypeInput()
    }


}