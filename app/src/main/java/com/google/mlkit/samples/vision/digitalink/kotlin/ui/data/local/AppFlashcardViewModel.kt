package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AppFlashcardViewModel(private val repository: AppRepository):ViewModel() {

    // --- Folder Operations ---

    private val _allFolders = MutableLiveData<List<Folder>>()
    val allFolders: LiveData<List<Folder>> get() = _allFolders

    fun insertFolder(folder: Folder) {
        viewModelScope.launch {
            repository.insertFolder(folder)
            fetchAllFolders() // Refresh the list
        }
    }

    fun updateFolder(folder: Folder) {
        viewModelScope.launch {
            repository.updateFolder(folder)
            fetchAllFolders() // Refresh the list
        }
    }

    fun getFolderById(folderId: Long): LiveData<Folder?> {
        val folderData = MutableLiveData<Folder?>()
        viewModelScope.launch {
            folderData.value = repository.getFolderById(folderId)
        }
        return folderData
    }

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            repository.deleteFolder(folder)
            fetchAllFolders() // Refresh the list
        }
    }

     fun fetchAllFolders() {
        viewModelScope.launch {
            _allFolders.value = repository.getAllFolders()
        }
    }


    // --- Lesson Operations ---

    private val _allLessons = MutableLiveData<List<Lesson>>()
    val allLessons: LiveData<List<Lesson>> get() = _allLessons

    fun insertLesson(lesson: Lesson) {
        viewModelScope.launch {
            repository.insertLesson(lesson)
            fetchAllLessons() // Refresh the list
        }
    }

    fun updateLesson(lesson: Lesson) {
        viewModelScope.launch {
            repository.updateLesson(lesson)
            fetchAllLessons() // Refresh the list
        }
    }

    fun getLessonById(lessonId: Long): LiveData<Lesson?> {
        val lessonData = MutableLiveData<Lesson?>()
        viewModelScope.launch {
            lessonData.value = repository.getLessonById(lessonId)
        }
        return lessonData
    }

    fun getLessonsByFolderId(folderId: Long): LiveData<List<Lesson>> {
        val lessonsData = MutableLiveData<List<Lesson>>()
        viewModelScope.launch {
            lessonsData.value = repository.getLessonsByFolderId(folderId)
        }
        return lessonsData
    }

    fun deleteLesson(lesson: Lesson) {
        viewModelScope.launch {
            repository.deleteLesson(lesson)
            fetchAllLessons() // Refresh the list
        }
    }

     fun fetchAllLessons() {
        viewModelScope.launch {
            _allLessons.value = repository.getAllLessons()
        }
    }


    // --- Flashcard Operations ---

    private val _allFlashcards = MutableLiveData<List<Flashcard>>()
    val allFlashcards: LiveData<List<Flashcard>> get() = _allFlashcards

    fun insertFlashcard(flashcard: Flashcard) {
        viewModelScope.launch {
            repository.insertFlashcard(flashcard)
            fetchAllFlashcards() // Refresh the list
        }
    }

    fun updateFlashcard(flashcard: Flashcard) {
        viewModelScope.launch {
            repository.updateFlashcard(flashcard)
            fetchAllFlashcards() // Refresh the list
        }
    }

    fun getFlashcardById(flashcardId: Long): LiveData<Flashcard?> {
        val flashcardData = MutableLiveData<Flashcard?>()
        viewModelScope.launch {
            flashcardData.value = repository.getFlashcardById(flashcardId)
        }
        return flashcardData
    }

    fun getFlashcardsByLessonId(lessonId: Long): LiveData<List<Flashcard>> {
        val flashcardsData = MutableLiveData<List<Flashcard>>()
        viewModelScope.launch {
            flashcardsData.value = repository.getFlashcardsByLessonId(lessonId)
        }
        return flashcardsData
    }

    fun deleteFlashcard(flashcard: Flashcard) {
        viewModelScope.launch {
            repository.deleteFlashcard(flashcard)
            fetchAllFlashcards() // Refresh the list
        }
    }

     fun fetchAllFlashcards() {
        viewModelScope.launch {
            _allFlashcards.value = repository.getAllFlashcards()
        }
    }


    // MutableLiveData for folder ID
    private val _folderId = MutableLiveData<Long>()
    val folderId: LiveData<Long> = _folderId

    // MutableLiveData for lesson ID
    private val _lessonId = MutableLiveData<Long>()
    val lessonId: LiveData<Long> = _lessonId

    // Function to update the folder ID
    fun setFolderId(newId: Long) {
        _folderId.value = newId
    }

    // Function to update the lesson ID
    fun setLessonId(newId: Long) {
        _lessonId.value = newId
    }


}