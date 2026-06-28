package com.google.mlkit.samples.vision.digitalink.kotlin.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SessionTopBar(drawerState: DrawerState, scope: CoroutineScope, viewModel: AppDatabaseViewModel) {
        val context = LocalContext.current
        var expanded by remember { mutableStateOf(false) }

        // Launcher for saving the backup file
        val createDocumentLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.CreateDocument("application/json")
        ) { uri ->
            uri?.let {
                scope.launch {
                    try {
                        val json = viewModel.exportDatabaseToJson()
                        context.contentResolver.openOutputStream(it)?.use { outputStream ->
                            outputStream.write(json.toByteArray())
                            Toast.makeText(context, "Backup exported successfully", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed to export backup: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        // Launcher for opening the backup file
        val openDocumentLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri?.let {
                scope.launch {
                    try {
                        val stringBuilder = StringBuilder()
                        context.contentResolver.openInputStream(it)?.use { inputStream ->
                            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                                var line: String? = reader.readLine()
                                while (line != null) {
                                    stringBuilder.append(line)
                                    line = reader.readLine()
                                }
                            }
                        }
                        viewModel.importDatabaseFromJson(stringBuilder.toString())
                        Toast.makeText(context, "Backup imported successfully", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed to import backup: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Lessons")
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch { drawerState.open() }
                }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Export Backup") },
                            onClick = {
                                expanded = false
                                createDocumentLauncher.launch("Bangla_Banan_Backup.json")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Import Backup") },
                            onClick = {
                                expanded = false
                                openDocumentLauncher.launch(arrayOf("application/json"))
                            }
                        )
                    }
                }
            }
        )
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionCompleteTopBar(navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Session completed")
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch { drawerState.open() }
            }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("home_screen") }) {
                Icon(Icons.Outlined.Home, contentDescription = "Home")
            }
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Edit")
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = { navController.popBackStack()}) {
                Icon(Icons.Default.Check, contentDescription = "More options")
            }
        }
    )
}

