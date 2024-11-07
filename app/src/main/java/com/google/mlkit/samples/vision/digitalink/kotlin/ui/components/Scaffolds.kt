package com.google.mlkit.samples.vision.digitalink.kotlin.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete.ToggleSegmentedButton
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.list.DemoNormalList
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppFlashcardViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.Lesson
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.MainViewModel
import kotlinx.coroutines.CoroutineScope

// Function to create TopAppBar and Scaffold
@Composable
fun MainScaffold(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    viewModel: AppFlashcardViewModel
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    val myViewModel: MainViewModel = viewModel()
    val isEdit by myViewModel.isPractice.collectAsState()

    Scaffold(
        topBar = { SessionTopBar(drawerState, scope) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                SegmentedButton()

                // Animated visibility for "entered edit mode" message
                AnimatedVisibility(
                    visible = !isEdit,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Entered edit mode")
                    }
                }

                DemoNormalList(navController,viewModel)
            }

            // FAB with icon and text at the bottom-right
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("Add") },
                onClick = { isDialogOpen = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )


            val folderId by viewModel.folderId.observeAsState()


            // Dialog for text input
            if (isDialogOpen) {
                AlertDialog(
                    onDismissRequest = { isDialogOpen = false },
                    title = { Text("Enter Text") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                label = { Text("Input") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                // Handle OK action here, like saving the inputText

                                //todo needs more safety checks
                                if( folderId != null){
                                    val lesson = Lesson(lessonName = inputText, folderOwnerId = folderId!!,)
                                    viewModel.insertLesson(lesson)
                                }




                                isDialogOpen = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { isDialogOpen = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun InputDialog(isDialogOpen: MutableState<Boolean>, inputText: MutableState<String>) {

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
            //DemoNormalList(navController, viewModel)

        }

    }
}
