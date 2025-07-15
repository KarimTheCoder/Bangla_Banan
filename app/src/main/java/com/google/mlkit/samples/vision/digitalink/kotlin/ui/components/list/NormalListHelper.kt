package com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.list

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Lesson
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.MainViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.practice.FlashcardViewModel


@Composable
fun NormalList(
    titles: List<Lesson>,
    onItemClick: (Lesson) -> Unit,
    viewModel: AppDatabaseViewModel,
    isEdit: Boolean
) {  // Add onItemClick callback




    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)  // Fill the available space
    ) {
        items(titles) { lesson ->

            val progress by viewModel.getLessonProgress(lesson.lessonId).observeAsState(initial = 0f)
            val familiarityProgress by viewModel.getLessonFamiliarityProgress(lesson.lessonId).observeAsState(initial = 0f)
            val spacedProgress by viewModel.getLessonSpacedRepetitionProgress(lesson.lessonId).observeAsState(initial = 0f)


            NormalListItem(
                title = lesson.lessonName,
                level1Progress = familiarityProgress,
                level1Text = "Familiarized",
                level2Progress = spacedProgress,
                level2Text = "Spaced repetition",
                additionalText = "${progress.toInt()}% completed",
                onItemClick = {


                    onItemClick(lesson)
                    viewModel.setLessonId(lesson.lessonId)


                },isEdit
            )

//            NormalListItem(title = title.lessonName, onItemClick = {
//
//
//                onItemClick(title)
//                viewModel.setLessonId( title.lessonId)
//
//
//            }
//
//            )  // Pass click callback
        }
    }
}

@Composable
fun NormalListItem(title: String, onItemClick: () -> Unit) {  // Add onItemClick callback

    Card(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 4.dp)
            .clickable { onItemClick() },  // Handle click here
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),  // Padding around each title
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun NormalListItem(
    title: String,
    level1Progress: Float,
    level1Text: String,
    level2Progress: Float,
    level2Text: String,
    additionalText: String,
    onItemClick: () -> Unit,
    isEdit: Boolean
    // New parameter for the text before the dropdown icon
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val editModeColor = MaterialTheme.colorScheme.tertiaryContainer // Light orange for edit mode
    val normalColor = MaterialTheme.colorScheme.surfaceVariant

    Card(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 4.dp)
            .clickable { onItemClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEdit) normalColor else editModeColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize() // Added animation for size changes
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // Ensures proper alignment
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    style = MaterialTheme.typography.titleSmall
                )

                Text( // New TextView before the dropdown icon
                    text = additionalText,
                    modifier = Modifier.padding(end = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                IconButton(
                    modifier = Modifier.padding(0.dp),
                    onClick = { isDropdownExpanded = !isDropdownExpanded }
                ) {
                    Icon(
                        imageVector = if (isDropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isDropdownExpanded) "Collapse" else "Expand"
                    )
                }
            }

            if (isDropdownExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
                ) {
                    ProgressBarWithText(
                        progress = level1Progress,
                        text = level1Text
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProgressBarWithText(
                        progress = level2Progress,
                        text = level2Text
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressBarWithText(progress: Float, text: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun DemoNormalList(
    navController: NavController,
    viewModel: AppDatabaseViewModel,
    flashcardVM: FlashcardViewModel,
    isEdit: Boolean
) {

    val myViewModel: MainViewModel = viewModel()

    // Use the ViewModel to observe state, call functions, etc.
    val isPractice by myViewModel.isPractice.collectAsState()

    val folderId by viewModel.folderId.observeAsState()

    // Observe lessons only when lessonId is non-null


    //val items by viewModel.allFolders.observeAsState(emptyList())


    val lessons by viewModel.allLessons.observeAsState(emptyList())

    NormalList(titles = lessons, onItemClick = { title ->
        // Handle the item click, for example, show a toast or log the title


        flashcardVM.setLessonId(title.lessonId)
        flashcardVM.loadFlashcards()

        if(isPractice){

            navController.navigate("practice_screen")
            Log.d("TitleListDemo", "Clicked on: ${title.lessonName} going to practice")


        }else{
            navController.navigate("edit_screen")
            Log.d("TitleListDemo", "Clicked on: ${title.lessonName} going to Edit")
        }

    }, viewModel, isEdit)
}