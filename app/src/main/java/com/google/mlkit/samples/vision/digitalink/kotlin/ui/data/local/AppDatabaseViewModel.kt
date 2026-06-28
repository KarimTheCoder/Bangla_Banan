package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.repo.AppRepository
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.AppBackup
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Folder
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Lesson
import kotlinx.coroutines.launch

class AppDatabaseViewModel(private val repository: AppRepository):ViewModel() {

    // --- Backup & Restore ---

    suspend fun exportDatabaseToJson(): String {
        val backup = repository.createBackupSnapshot()
        return Gson().toJson(backup)
    }

    suspend fun importDatabaseFromJson(jsonString: String) {
        val backup = Gson().fromJson(jsonString, AppBackup::class.java)
        if (backup != null) {
            repository.restoreBackupSnapshot(backup)
            refreshDrawerContent() // Refresh UI after import
            fetchAllFolders()
            val folderId = _folderId.value
            if (folderId != null) {
                fetchAllLessons(folderId)
            }
        }
    }

    // MutableLiveData for folder ID (the currently selected leaf folder for lessons)
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


    // --- Folder Navigation State (for drill-down drawer) ---

    // The folder the user is currently viewing in the drawer (null = root level)
    private val _currentParentFolder = MutableLiveData<Folder?>(null)
    val currentParentFolder: LiveData<Folder?> = _currentParentFolder

    // Navigation stack for back navigation in the drawer
    private val _folderNavigationStack = mutableListOf<Folder?>()

    // The folders currently displayed in the drawer
    private val _drawerFolders = MutableLiveData<List<Folder>>()
    val drawerFolders: LiveData<List<Folder>> get() = _drawerFolders

    // The lessons displayed in the drawer for the current sub-folder
    private val _drawerLessons = MutableLiveData<List<Lesson>>()
    val drawerLessons: LiveData<List<Lesson>> get() = _drawerLessons

    // Navigate into a folder (drill down)
    fun navigateIntoFolder(folder: Folder) {
        _folderNavigationStack.add(_currentParentFolder.value)
        _currentParentFolder.value = folder
        refreshDrawerContent()
    }

    // Navigate up one level in the folder hierarchy
    fun navigateUp(): Boolean {
        if (_folderNavigationStack.isEmpty()) return false
        val parent = _folderNavigationStack.removeAt(_folderNavigationStack.size - 1)
        _currentParentFolder.value = parent
        refreshDrawerContent()
        return true
    }

    // Check if we can navigate up
    fun canNavigateUp(): Boolean {
        return _folderNavigationStack.isNotEmpty()
    }

    // Get current navigation depth (0 = root)
    fun getNavigationDepth(): Int {
        return _folderNavigationStack.size
    }

    // Refresh the drawer content based on the current parent folder
    fun refreshDrawerContent() {
        viewModelScope.launch {
            val parent = _currentParentFolder.value
            if (parent == null) {
                // At root level: show root folders
                _drawerFolders.value = repository.getRootFolders()
                _drawerLessons.value = emptyList()
            } else {
                // Inside a folder: show sub-folders and lessons
                _drawerFolders.value = repository.getChildFolders(parent.folderId)
                _drawerLessons.value = repository.getLessonsByFolderId(parent.folderId)
            }
        }
    }


    // --- Folder Operations ---

    private val _allFolders = MutableLiveData<List<Folder>>()
    val allFolders: LiveData<List<Folder>> get() = _allFolders

    fun insertFolder(folder: Folder) {
        viewModelScope.launch {
            repository.insertFolder(folder)
            refreshDrawerContent()
            fetchAllFolders() // Also refresh allFolders for backward compat
        }
    }

    fun updateFolder(folder: Folder) {
        viewModelScope.launch {
            repository.updateFolder(folder)
            refreshDrawerContent()
            fetchAllFolders()
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
            refreshDrawerContent()
            fetchAllFolders()
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

    fun loadFlashcardsByLessonId(lessonId: Long?) {
        if (lessonId != null) {
            viewModelScope.launch {
                _allFlashcards.postValue(repository.getFlashcardsByLessonId(lessonId))
            }
        } else {
            _allFlashcards.postValue(emptyList()) // Clear the list if lessonId is null
        }
    }


    fun insertFlashcard(flashcard: Flashcard) {
        viewModelScope.launch {
            repository.insertFlashcard(flashcard)
            loadFlashcardsByLessonId(flashcard.lessonOwnerId) // Refresh the list
        }
    }

    fun insertFlashcards(flashcards: List<Flashcard>) {
        viewModelScope.launch {
            flashcards.forEach { repository.insertFlashcard(it) }
            if (flashcards.isNotEmpty()) {
                loadFlashcardsByLessonId(flashcards.first().lessonOwnerId)
            }
        }
    }

    fun updateFlashcard(flashcard: Flashcard) {
        viewModelScope.launch {
            repository.updateFlashcard(flashcard)
            loadFlashcardsByLessonId(flashcard.lessonOwnerId) // Refresh the list
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
            loadFlashcardsByLessonId(flashcard.lessonOwnerId) // Refresh the list
        }
    }

     private fun fetchAllFlashcards() {
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