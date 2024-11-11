package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room

import androidx.room.Embedded
import androidx.room.Relation

// Folder with Lessons
data class FolderWithLessons(
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "folderOwnerId"
    )
    val lessons: List<LessonWithFlashcards>
)

// Lesson with Flashcards
data class LessonWithFlashcards(
    @Embedded val lesson: Lesson,
    @Relation(
        parentColumn = "lessonId",
        entityColumn = "lessonOwnerId"
    )
    val flashcards: List<Flashcard>
)