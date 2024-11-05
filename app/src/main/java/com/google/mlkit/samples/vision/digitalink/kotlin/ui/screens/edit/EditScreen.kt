package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.EditSegmentedButton
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.EditTopBar
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.FlashcardViewModel

@Composable
fun EditScreen(navController: NavController, viewModel: FlashcardViewModel) {

    Column {
        EditTopBar(navController)
        EditSegmentedButton()
        TypeInput(viewModel)
    }


}