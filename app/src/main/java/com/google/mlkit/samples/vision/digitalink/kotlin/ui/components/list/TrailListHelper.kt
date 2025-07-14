package com.google.mlkit.samples.vision.digitalink.kotlin.ui.components.list
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard


@Composable
fun ListWithTrail(flashcards: List<Flashcard>, onDeleteClick: (Flashcard) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(flashcards) { flashcard ->

            ListItemWithTrail(flashcard, onTrailClick = onDeleteClick)
        }
    }
}

@Composable
fun ListItemWithTrail(flashcard: Flashcard, onTrailClick: (Flashcard) -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 4.dp)
            .clickable { }, // You can handle the click here
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = flashcard.word,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bars
                LabelWithProgress(label = "Familiarity", value = flashcard.familiarityCount)
                LabelWithProgress(label = "Spaced repetition", value = flashcard.boxLevel)
                Spacer(modifier = Modifier.height(6.dp))

                Spacer(modifier = Modifier.height(6.dp))

                // Due date badge
                DueBadge(flashcard.getDueDateString())
            }

            IconButton(onClick = { onTrailClick(flashcard) }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete ${flashcard.word}",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun LabelWithProgress(label: String, value: Int) {
    Column {
        Text(
            text = "$label: $value/5",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
        LinearProgressIndicator(
            progress = (value.coerceIn(0, 5)) / 5f,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
            color =
                MaterialTheme.colorScheme.primary,
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun DueBadge(dueDate: String) {
    Box(
        modifier = Modifier
            .padding(top = 4.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Calendar icon",
                modifier = Modifier
                    .size(14.dp)
                    .padding(end = 4.dp),
                tint = Color.White
            )
            Text(
                text = "Due: $dueDate",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DemoTrailList(flashcards: List<Flashcard>, viewModel: AppDatabaseViewModel) {


    ListWithTrail(flashcards = flashcards, onDeleteClick = { item ->
        // Handle delete action for the clicked item
        viewModel.deleteFlashcard(item)
        println("Deleted: $item")
    })
}