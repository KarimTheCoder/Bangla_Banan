package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.repo.AppRepository
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Folder
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Lesson
import kotlinx.coroutines.launch

class AppDatabaseViewModel(private val repository: AppRepository):ViewModel() {

    // MutableLiveData for folder ID
    private val _folderId = MutableLiveData<Long>()
    val folderId: LiveData<Long> = _folderId
    // Function to check if folderId is initialized
    fun isFolderIdInitialized(): Boolean {
        return _folderId.value != null
    }

    // MutableLiveData for lesson ID
    private val _lessonId = MutableLiveData<Long>()
    val lessonId: LiveData<Long> = _lessonId

    // Function to update the folder ID
    fun setFolderId(newId: Long) {
        _folderId.value = newId
        fetchAllLessons(newId)
    }

    // Function to update the lesson ID
    fun setLessonId(newId: Long) {
        _lessonId.value = newId
    }



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

    fun insertLesson(lesson: Lesson,folderId: Long) {
        viewModelScope.launch {
            repository.insertLesson(lesson)
            fetchAllLessons(folderId) // Refresh the list
        }
    }

    fun updateLesson(lesson: Lesson,folderId: Long) {
        viewModelScope.launch {
            repository.updateLesson(lesson)
            fetchAllLessons(folderId) // Refresh the list
        }
    }

    fun getLessonById(lessonId: Long): LiveData<Lesson?> {
        val lessonData = MutableLiveData<Lesson?>()
        viewModelScope.launch {
            lessonData.value = repository.getLessonById(lessonId)
        }
        return lessonData
    }




    fun getLessonsByFolderId(folderId: Long?){

        viewModelScope.launch {
            _allLessons.value = repository.getLessonsByFolderId(folderId)
        }
        //return lessonsData

    }

    fun deleteLesson(lesson: Lesson,folderId: Long) {
        viewModelScope.launch {
            repository.deleteLesson(lesson)
            fetchAllLessons(folderId) // Refresh the list
        }
    }

     fun fetchAllLessons(folderId: Long) {

        viewModelScope.launch {

            //todo null check
                _allLessons.value = repository.getLessonsByFolderId(folderId)
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



    fun getLessonProgress(lessonId: Long): LiveData<Float> = liveData {
        val progress = repository.calculateLessonProgress(lessonId)
        emit(progress)
    }

    fun getLessonFamiliarityProgress(lessonId: Long): LiveData<Float> = liveData {
        val progress = repository.calculateFamiliarityCountProgress(lessonId)
        emit(progress)
    }
    fun getLessonSpacedRepetitionProgress(lessonId: Long): LiveData<Float> = liveData {
        val progress = repository.calculateBoxLevelProgress(lessonId)
        emit(progress)
    }


}