package com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard


@Composable
fun ListWithTrail(titles: List<Flashcard>, onDeleteClick: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(titles) { title ->
            ListItemWithTrail(title = title.word, onTrailClick = onDeleteClick)
        }
    }
}

@Composable
fun ListItemWithTrail(title: String, onTrailClick: (String) -> Unit) {

    Card(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 4.dp)
            .clickable {  },  // Handle click here
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Space between title and icon
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f) // Pushes the icon to the end
            )
            IconButton(onClick = { onTrailClick(title) }) {
                Icon(
                    imageVector = Icons.Outlined.Delete, // Trash icon
                    contentDescription = "Delete $title",
                    tint = Color.Gray
                )
            }
        }
    }
}
@Composable
fun DemoTrailList(lessons: List<Flashcard>) {
    val titles = listOf("Item 1", "Item 2", "Item 3")


    ListWithTrail(titles = lessons, onDeleteClick = { item ->
        // Handle delete action for the clicked item
        println("Deleted: $item")
    })
}