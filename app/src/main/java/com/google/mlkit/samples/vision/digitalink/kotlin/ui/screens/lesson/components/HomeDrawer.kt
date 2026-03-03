package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Folder
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Lesson
import com.samsulkarim.bananapp.R
import kotlinx.coroutines.CoroutineScope


@Composable
fun HomeDrawer(
    viewModel: AppDatabaseViewModel,
    scaffoldContent: @Composable (drawerState: DrawerState, scope: CoroutineScope) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Load initial drawer content
    LaunchedEffect(Unit) {
        viewModel.refreshDrawerContent()
        viewModel.fetchAllFolders()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(viewModel) }
    ) {
        scaffoldContent(drawerState, scope)
    }
}

@Composable
fun DrawerContent(
    viewModel: AppDatabaseViewModel
) {
    val drawerFolders by viewModel.drawerFolders.observeAsState(emptyList())
    val drawerLessons by viewModel.drawerLessons.observeAsState(emptyList())
    val currentParent by viewModel.currentParentFolder.observeAsState(null)
    val navigationDepth = viewModel.getNavigationDepth()

    val isDialogOpen = remember { mutableStateOf(false) }
    val dialogInputName = remember { mutableStateOf("") }

    // Determine what adding means at this level:
    // Root level (depth 0): add a main folder (parentFolderId = null)
    // Inside a main folder (depth 1): add a sub-folder (parentFolderId = currentParent.folderId)
    // Inside a sub-folder (depth 2+): add a lesson

    val isAtRoot = currentParent == null
    val isAtMainFolder = !isAtRoot && navigationDepth == 1
    val isAtSubFolder = !isAtRoot && navigationDepth >= 2

    ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header with back button and current folder name
            if (currentParent != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    IconButton(onClick = { viewModel.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        text = currentParent!!.folderName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
                HorizontalDivider()
            } else {
                Text(
                    "Folders",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
            }

            // Content: folders and/or lessons
            AnimatedContent(
                targetState = currentParent,
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    slideInHorizontally { width -> width } togetherWith
                    slideOutHorizontally { width -> -width }
                },
                label = "drawer_content_animation"
            ) { parent ->
                LazyColumn {
                    // Show sub-folders
                    if (drawerFolders.isNotEmpty()) {
                        item {
                            Text(
                                text = if (isAtRoot) "Main Folders" else "Sub-Folders",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Gray
                            )
                        }
                        items(drawerFolders) { folder ->
                            NavigationDrawerItem(
                                label = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = folder.folderName,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                },
                                selected = false,
                                icon = {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_folder_24),
                                        contentDescription = "Folder"
                                    )
                                },
                                onClick = {
                                    viewModel.navigateIntoFolder(folder)
                                }
                            )
                        }
                    }

                    // Show lessons (only inside sub-folders, depth >= 2)
                    if (isAtSubFolder && drawerLessons.isNotEmpty()) {
                        item {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            Text(
                                text = "Lessons",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Gray
                            )
                        }
                        items(drawerLessons) { lesson ->
                            NavigationDrawerItem(
                                label = {
                                    Text(text = lesson.lessonName)
                                },
                                selected = false,
                                icon = {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_folder_24),
                                        contentDescription = "Lesson"
                                    )
                                },
                                onClick = {
                                    // Select this sub-folder as the active folder and lesson
                                    viewModel.setFolderId(lesson.folderOwnerId)
                                }
                            )
                        }
                    }

                    // Empty state
                    if (drawerFolders.isEmpty() && (drawerLessons.isEmpty() || !isAtSubFolder)) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (isAtRoot) "No folders yet"
                                           else if (isAtMainFolder) "No sub-folders yet"
                                           else "No lessons or sub-folders yet",
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Add button at the bottom
            HorizontalDivider(Modifier.height(1.dp), color = Color.LightGray)

            val addButtonText = when {
                isAtRoot -> "Add Main Folder"
                isAtMainFolder -> "Add Sub-Folder"
                isAtSubFolder -> "Add Lesson"
                else -> "Add"
            }

            Button(
                onClick = { isDialogOpen.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(addButtonText)
            }

            // Dialog for input
            if (isDialogOpen.value) {
                val dialogTitle = when {
                    isAtRoot -> "Create Main Folder"
                    isAtMainFolder -> "Create Sub-Folder"
                    isAtSubFolder -> "Create Lesson"
                    else -> "Create"
                }
                val dialogLabel = when {
                    isAtRoot -> "Folder name"
                    isAtMainFolder -> "Sub-folder name"
                    isAtSubFolder -> "Lesson name"
                    else -> "Name"
                }

                NameInputDialog(
                    isDialogOpen = isDialogOpen,
                    inputName = dialogInputName,
                    title = dialogTitle,
                    label = dialogLabel,
                    onConfirm = {
                        when {
                            isAtRoot -> {
                                // Create a root-level main folder
                                val folder = Folder(
                                    folderName = dialogInputName.value,
                                    parentFolderId = null
                                )
                                viewModel.insertFolder(folder)
                            }
                            isAtMainFolder -> {
                                // Create a sub-folder inside the current main folder
                                val folder = Folder(
                                    folderName = dialogInputName.value,
                                    parentFolderId = currentParent!!.folderId
                                )
                                viewModel.insertFolder(folder)
                            }
                            isAtSubFolder -> {
                                // Create a lesson inside the current sub-folder
                                val lesson = Lesson(
                                    lessonName = dialogInputName.value,
                                    folderOwnerId = currentParent!!.folderId
                                )
                                viewModel.insertLesson(lesson, currentParent!!.folderId)
                            }
                        }
                        dialogInputName.value = ""
                    }
                )
            }
        }
    }
}


@Composable
fun NameInputDialog(
    isDialogOpen: MutableState<Boolean>,
    inputName: MutableState<String>,
    title: String,
    label: String,
    onConfirm: () -> Unit
) {
    if (isDialogOpen.value) {
        AlertDialog(
            onDismissRequest = { isDialogOpen.value = false },
            title = { Text(title) },
            text = {
                OutlinedTextField(
                    value = inputName.value,
                    onValueChange = { inputName.value = it },
                    label = { Text(label) },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        isDialogOpen.value = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isDialogOpen.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


// Data class to represent each Drawer item
data class DrawerItem(val title: String, val folderId: Long, val icon: @Composable () -> Unit, val trailingText: String?)
