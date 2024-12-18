package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Folder Entity
@Entity(tableName = "folder_table")
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val folderId: Long = 0,
    val folderName: String
){
    override fun toString(): String {
        return "Folder(folderId=$folderId, folderName='$folderName')"
    }
}

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
){

    override fun toString(): String {
        return "Lesson(lessonId=$lessonId, lessonName='$lessonName', folderOwnerId=$folderOwnerId)"
    }
}

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
    var dueDate: Long = System.currentTimeMillis(), // Next review date, based on box level
    var familiarityCount: Int = 0


)  {


    fun isFamiliarized():Boolean{
        return familiarityCount >= 5
    }

    fun getUnfamiliarMaxReviewCount(): Int {
        return maxOf(0, 5 - familiarityCount) // Ensures the result is not negative
    }

    fun getDueDateString(): String {

        // Define the date format you want (e.g., "yyyy-MM-dd HH:mm:ss")
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        // Convert Long to Date and format it to a string
        val dateString: String = dateFormat.format(Date(dueDate))
        return dateString;
    }

    override fun toString(): String {
        return "Flashcard(flashcardId=$flashcardId, word='$word', definition='$definition', " +
                "lessonOwnerId=$lessonOwnerId, isMistake=$isMistake, boxLevel=$boxLevel, " +
                "dueDate=$dueDate, familiarityCount=$familiarityCount)"
    }

}