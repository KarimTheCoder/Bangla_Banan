package com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppFlashcardViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.Lesson
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.MainViewModel


@Composable
fun NormalList(
    titles: List<Lesson>,
    onItemClick: (String) -> Unit,
    viewModel: AppFlashcardViewModel
) {  // Add onItemClick callback
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)  // Fill the available space
    ) {
        items(titles) { title ->


            NormalListItem(title = title.lessonName, onItemClick = {

                onItemClick(title.lessonName)
                viewModel.setLessonId( title.lessonId)

            }

            )  // Pass click callback
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
fun DemoNormalList(navController: NavController, viewModel: AppFlashcardViewModel) {
    val titles = listOf(
        "Title 1",
        "Title 2",
        "Title 3",
        "Title 4",
        "Title 5"
    )

    val myViewModel: MainViewModel = viewModel()

    // Use the ViewModel to observe state, call functions, etc.
    val isPractice by myViewModel.isPractice.collectAsState()

    val folderId by viewModel.folderId.observeAsState()

    // Observe lessons only when lessonId is non-null
    val lessons = folderId?.let {
        viewModel.getLessonsByFolderId(it).observeAsState(emptyList()).value
    } ?: emptyList()

    NormalList(titles = lessons, onItemClick = { title ->
        // Handle the item click, for example, show a toast or log the title




        if(isPractice){

            navController.navigate("practice_screen")



            Log.d("TitleListDemo", "Clicked on: $title going to practice")

        }else{

            navController.navigate("edit_screen")

            Log.d("TitleListDemo", "Clicked on: $title going to Edit")

        }
    }, viewModel)
}