package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local

class AppRepository(private val appDao: AppDao) {


    // --- Folder Operations ---

    // Insert a folder
    suspend fun insertFolder(folder: Folder) {
        appDao.insertFolder(folder)
    }

    // Update a folder
    suspend fun updateFolder(folder: Folder) {
        appDao.updateFolder(folder)
    }

    // Get a specific folder by ID
    suspend fun getFolderById(folderId: Long): Folder? {
        return appDao.getFolderById(folderId)
    }

    // Get all folders
    suspend fun getAllFolders(): List<Folder> {
        return appDao.getAllFolders()
    }

    // Delete a folder
    suspend fun deleteFolder(folder: Folder) {
        appDao.deleteFolder(folder)
    }


    // --- Lesson Operations ---

    // Insert a lesson
    suspend fun insertLesson(lesson: Lesson) {
        appDao.insertLesson(lesson)
    }

    // Update a lesson
    suspend fun updateLesson(lesson: Lesson) {
        appDao.updateLesson(lesson)
    }

    // Get a specific lesson by ID
    suspend fun getLessonById(lessonId: Long): Lesson? {
        return appDao.getLessonById(lessonId)
    }

    // Get all lessons
    suspend fun getAllLessons(): List<Lesson> {
        return appDao.getAllLessons()
    }

    // Get all lessons for a specific folder
    suspend fun getLessonsByFolderId(folderId: Long): List<Lesson> {
        return appDao.getLessonsByFolderId(folderId)
    }

    // Delete a lesson
    suspend fun deleteLesson(lesson: Lesson) {
        appDao.deleteLesson(lesson)
    }


    // --- Flashcard Operations ---

    // Insert a flashcard
    suspend fun insertFlashcard(flashcard: Flashcard) {
        appDao.insertFlashcard(flashcard)
    }

    // Update a flashcard
    suspend fun updateFlashcard(flashcard: Flashcard) {
        appDao.updateFlashcard(flashcard)
    }

    // Get a specific flashcard by ID
    suspend fun getFlashcardById(flashcardId: Long): Flashcard? {
        return appDao.getFlashcardById(flashcardId)
    }

    // Get all flashcards
    suspend fun getAllFlashcards(): List<Flashcard> {
        return appDao.getAllFlashcards()
    }

    // Get all flashcards for a specific lesson
    suspend fun getFlashcardsByLessonId(lessonId: Long): List<Flashcard> {
        return appDao.getFlashcardsByLessonId(lessonId)
    }

    // Delete a flashcard
    suspend fun deleteFlashcard(flashcard: Flashcard) {
        appDao.deleteFlashcard(flashcard)
    }

    // Load due flashcards from DAO
    suspend fun getDueFlashcards(): List<Flashcard> {

        return appDao.getDueFlashcards()
    }

    // Update a flashcard after review
    suspend fun updateFlashcardLeitner(flashcard: Flashcard, isCorrect: Boolean) {
        if (isCorrect) {
            // Move to the next box level (up to the max level of 5)
            flashcard.boxLevel = (flashcard.boxLevel + 1).coerceAtMost(5)
        } else {
            // Incorrect answer sends the card back to box 1
            flashcard.boxLevel = 1
        }

        // Set the due date based on box level
        val intervalDays = when (flashcard.boxLevel) {
            1 -> 1
            2 -> 3
            3 -> 7
            4 -> 14
            else -> 30
        }
        flashcard.dueDate = System.currentTimeMillis() + intervalDays * 24 * 60 * 60 * 1000L

        // Update flashcard in database
        appDao.updateFlashcard(flashcard)
    }
}