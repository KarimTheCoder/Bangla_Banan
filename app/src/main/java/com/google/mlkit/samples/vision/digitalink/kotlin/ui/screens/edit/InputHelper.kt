package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.edit

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.list.DemoTrailList
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard


@Composable
fun TypeInput(cardViewModel: AppDatabaseViewModel) {

    val context = LocalContext.current
    val viewModel: EditViewModel = viewModel()

    // Observe isExpanded and text from ViewModel
    val isExpanded by viewModel.isExpanded
    val info by viewModel.text

    val lessonId by cardViewModel.lessonId.observeAsState()

    if (isExpanded) {
        // --- Import Mode ---
        ImportInput(cardViewModel, viewModel, lessonId)
    } else {
        // --- Type Mode (existing) ---
        SingleWordInput(cardViewModel, viewModel, info, lessonId, context)
    }
}

@Composable
private fun ImportInput(
    cardViewModel: AppDatabaseViewModel,
    viewModel: EditViewModel,
    lessonId: Long?
) {
    val context = LocalContext.current
    var importText by remember { mutableStateOf("") }
    val importResult by viewModel.importResult

    Column {
        OutlinedTextField(
            value = importText,
            onValueChange = { importText = it },
            label = { Text("Paste words here") },
            placeholder = { Text("One word per line...\nExample:\nবাংলা\nবানান\nঅভ্যাস") },
            trailingIcon = {
                if (importText.isNotEmpty()) {
                    IconButton(onClick = { importText = "" }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear text")
                    }
                }
            },
            singleLine = false,
            minLines = 6,
            maxLines = 12,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Info or result text
        if (importResult.isNotEmpty()) {
            InfoTextWithIcon(importResult)
        } else {
            InfoTextWithIcon("Paste words below, one per line")
        }

        // Import button
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (lessonId == null) {
                        Toast.makeText(context, "No lesson selected", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val allLines = importText.lines()
                        .map { it.trim() }
                        .filter { it.isNotBlank() }

                    val banglaWords = allLines.filter { line ->
                        line.any { c -> c in '\u0980'..'\u09FF' }
                    }

                    val skipped = allLines.size - banglaWords.size

                    if (banglaWords.isEmpty()) {
                        Toast.makeText(context, "No valid Bangla words found", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val flashcards = banglaWords.map { word ->
                        Flashcard(
                            word = word,
                            definition = word,
                            lessonOwnerId = lessonId!!
                        )
                    }

                    cardViewModel.insertFlashcards(flashcards)
                    viewModel.setImportResult(banglaWords.size, skipped)
                    importText = "" // Clear after import
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Import icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Import All")
                }
            }
        }

        // Show existing flashcards
        val flashcards by cardViewModel.allFlashcards.observeAsState(emptyList())
        LaunchedEffect(lessonId) {
            cardViewModel.loadFlashcardsByLessonId(lessonId)
        }
        DemoTrailList(flashcards, cardViewModel)
    }
}

@Composable
private fun SingleWordInput(
    cardViewModel: AppDatabaseViewModel,
    viewModel: EditViewModel,
    info: String,
    lessonId: Long?,
    context: android.content.Context
) {
    var text by remember { mutableStateOf("") }
    val textFieldHeight by animateDpAsState(targetValue = 80.dp, label = "")

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            label = { Text("Create a flashcard") },
            placeholder = { Text("Type here...") },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(onClick = { text = "" }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear text")
                    }
                }
            },
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(textFieldHeight)
                .padding(start = 24.dp, end = 24.dp, top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        InfoTextWithIcon(info)

        // Pass the text value to the button's onClick function
        AddButton(onClick = {
            val formattedText = text.trim()
            text = "" // Clear input

            val isBangla = formattedText.any { it in '\u0980'..'\u09FF' }

            if (isBangla) {
                viewModel.updateText("$formattedText was added")

                val flashcard = lessonId?.let {
                    Flashcard(word = formattedText, definition = formattedText, lessonOwnerId = it)
                }

                if (flashcard != null) {
                    cardViewModel.insertFlashcard(flashcard)
                } else {
                    Log.e("FlashcardInsert", "Flashcard not inserted: lessonId is null")
                }
            } else {
                Toast.makeText(context, "Please enter Bangla text only.", Toast.LENGTH_SHORT).show()
            }
        })

        // Observe lessons only when lessonId is non-null
        val flashcards by cardViewModel.allFlashcards.observeAsState(emptyList())
        LaunchedEffect(lessonId) {
            cardViewModel.loadFlashcardsByLessonId(lessonId)
        }
        DemoTrailList(flashcards, cardViewModel)
    }
}

@Composable
fun InfoTextWithIcon(info: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = info,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add")
            }
        }
    }
}