package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local

class AppRepository(private val folderDao: FolderDao) {


    // --- Folder Operations ---

    // Insert a folder
    suspend fun insertFolder(folder: Folder) {
        folderDao.insertFolder(folder)
    }

    // Update a folder
    suspend fun updateFolder(folder: Folder) {
        folderDao.updateFolder(folder)
    }

    // Get a specific folder by ID
    suspend fun getFolderById(folderId: Long): Folder? {
        return folderDao.getFolderById(folderId)
    }

    // Get all folders
    suspend fun getAllFolders(): List<Folder> {
        return folderDao.getAllFolders()
    }

    // Delete a folder
    suspend fun deleteFolder(folder: Folder) {
        folderDao.deleteFolder(folder)
    }


    // --- Lesson Operations ---

    // Insert a lesson
    suspend fun insertLesson(lesson: Lesson) {
        folderDao.insertLesson(lesson)
    }

    // Update a lesson
    suspend fun updateLesson(lesson: Lesson) {
        folderDao.updateLesson(lesson)
    }

    // Get a specific lesson by ID
    suspend fun getLessonById(lessonId: Long): Lesson? {
        return folderDao.getLessonById(lessonId)
    }

    // Get all lessons
    suspend fun getAllLessons(): List<Lesson> {
        return folderDao.getAllLessons()
    }

    // Get all lessons for a specific folder
    suspend fun getLessonsByFolderId(folderId: Long): List<Lesson> {
        return folderDao.getLessonsByFolderId(folderId)
    }

    // Delete a lesson
    suspend fun deleteLesson(lesson: Lesson) {
        folderDao.deleteLesson(lesson)
    }


    // --- Flashcard Operations ---

    // Insert a flashcard
    suspend fun insertFlashcard(flashcard: Flashcard) {
        folderDao.insertFlashcard(flashcard)
    }

    // Update a flashcard
    suspend fun updateFlashcard(flashcard: Flashcard) {
        folderDao.updateFlashcard(flashcard)
    }

    // Get a specific flashcard by ID
    suspend fun getFlashcardById(flashcardId: Long): Flashcard? {
        return folderDao.getFlashcardById(flashcardId)
    }

    // Get all flashcards
    suspend fun getAllFlashcards(): List<Flashcard> {
        return folderDao.getAllFlashcards()
    }

    // Get all flashcards for a specific lesson
    suspend fun getFlashcardsByLessonId(lessonId: Long): List<Flashcard> {
        return folderDao.getFlashcardsByLessonId(lessonId)
    }

    // Delete a flashcard
    suspend fun deleteFlashcard(flashcard: Flashcard) {
        folderDao.deleteFlashcard(flashcard)
    }
}