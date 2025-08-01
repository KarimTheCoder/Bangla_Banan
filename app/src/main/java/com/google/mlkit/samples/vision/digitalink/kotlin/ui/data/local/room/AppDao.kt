package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppDao {

    // --- Folder CRUD Operations ---

    // Insert a Folder
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: Folder)

    // Update a Folder
    @Update
    suspend fun updateFolder(folder: Folder)

    // Query a specific Folder by ID
    @Query("SELECT * FROM folder_table WHERE folderId = :folderId")
    suspend fun getFolderById(folderId: Long): Folder?

    // Query all Folders
    @Query("SELECT * FROM folder_table")
    suspend fun getAllFolders(): List<Folder>

    // Delete a Folder
    @Delete
    suspend fun deleteFolder(folder: Folder)


    // --- Lesson CRUD Operations ---

    // Insert a Lesson
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: Lesson)

    // Update a Lesson
    @Update
    suspend fun updateLesson(lesson: Lesson)

    // Query a specific Lesson by ID
    @Query("SELECT * FROM lesson_table WHERE lessonId = :lessonId")
    suspend fun getLessonById(lessonId: Long): Lesson?

    // Query all Lessons
    @Query("SELECT * FROM lesson_table")
    suspend fun getAllLessons(): List<Lesson>

    // Query all Lessons in a specific Folder
    @Query("SELECT * FROM lesson_table WHERE folderOwnerId = :folderId")
    suspend fun getLessonsByFolderId(folderId: Long?): List<Lesson>

    // Delete a Lesson
    @Delete
    suspend fun deleteLesson(lesson: Lesson)


    // --- Flashcard CRUD Operations ---

    // Insert a Flashcard
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: Flashcard)

    // Update a Flashcard
    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)

    // Query a specific Flashcard by ID
    @Query("SELECT * FROM flashcard_table WHERE flashcardId = :flashcardId")
    suspend fun getFlashcardById(flashcardId: Long): Flashcard?

    // Query all Flashcards
    @Query("SELECT * FROM flashcard_table")
    suspend fun getAllFlashcards(): List<Flashcard>

    // Query all Flashcards in a specific Lesson
    @Query("SELECT * FROM flashcard_table WHERE lessonOwnerId = :lessonId")
    suspend fun getFlashcardsByLessonId(lessonId: Long): List<Flashcard>

    // Delete a Flashcard
    @Delete
    suspend fun deleteFlashcard(flashcard: Flashcard)

    @Query("SELECT * FROM flashcard_table WHERE dueDate <= :currentTime")
    suspend fun getDueFlashcards(currentTime: Long = System.currentTimeMillis()): List<Flashcard>

    @Query("SELECT * FROM flashcard_table WHERE dueDate <= :currentTime AND lessonOwnerId = :lessonId")
    suspend fun getDueFlashcardsByLesson(lessonId: Long, currentTime: Long = System.currentTimeMillis()): List<Flashcard>



    // Function to get the count of familiarized flashcards
    @Query("SELECT COUNT(*) FROM flashcard_table WHERE familiarityCount >= 5")
    suspend fun getFamiliarizedFlashcardCount(): Int

    // Function to get the count of not familiarized flashcards
    @Query("SELECT COUNT(*) FROM flashcard_table WHERE familiarityCount < 5")
    suspend fun getNotFamiliarizedFlashcardCount(): Int

    // Function to get a specific number of familiarized flashcards
    @Query("SELECT * FROM flashcard_table WHERE dueDate <= :currentTime AND familiarityCount >= 5 AND lessonOwnerId = :lessonId LIMIT :limit")
    suspend fun getDueCards(limit: Int, lessonId: Long, currentTime: Long = System.currentTimeMillis()): List<Flashcard>

    // Function to get a specific number of not familiarized flashcards
    @Query("SELECT * FROM flashcard_table WHERE familiarityCount < 5 AND lessonOwnerId = :lessonId LIMIT :limit")
    suspend fun getNotFamiliarizedFlashcards(limit: Int, lessonId: Long): List<Flashcard>
}