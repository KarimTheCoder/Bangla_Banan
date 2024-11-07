package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Folder Entity
@Entity(tableName = "folder_table")
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val folderId: Long = 0,
    val folderName: String
)

// Lesson Entity
@Entity(
    tableName = "lesson_table",
    foreignKeys = [ForeignKey(
        entity = Folder::class,
        parentColumns = ["folderId"],
        childColumns = ["folderOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("folderOwnerId")]
)
data class Lesson(
    @PrimaryKey(autoGenerate = true) val lessonId: Long = 0,
    val lessonName: String,
    val folderOwnerId: Long // Foreign key referring to the Folder table
)

@Entity(
    tableName = "flashcard_table",
    foreignKeys = [ForeignKey(
        entity = Lesson::class,
        parentColumns = ["lessonId"],
        childColumns = ["lessonOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("lessonOwnerId")]
)
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val flashcardId: Long = 0,
    val word: String,
    val definition: String,
    val lessonOwnerId: Long,
    val isMistake: Boolean = false,
    var boxLevel: Int = 1, // Starts in the first box by default
    var dueDate: Long = System.currentTimeMillis() // Next review date, based on box level
)