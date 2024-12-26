package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Flashcard
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Folder
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Lesson


    const val filename = "bancom.json"

fun loadJsonData(context: Context): List<Flashcard> {
    val json = context.assets.open(filename).bufferedReader().use { it.readText() }
    val listType = object : TypeToken<List<Flashcard>>() {}.type
    return Gson().fromJson(json, listType)
}
fun loadLessonsFromJsonData(context: Context): List<Lesson> {
    val json = context.assets.open(filename).bufferedReader().use { it.readText() }
    val listType = object : TypeToken<List<Lesson>>() {}.type
    val lessons: List<Lesson> = Gson().fromJson(json, listType)

    // Filter unique lessons based on lessonId
    return lessons.distinctBy { it.lessonId }
}

fun loadFolderFromJsonData(context: Context): List<Folder> {
    val json = context.assets.open(filename).bufferedReader().use { it.readText() }
    val listType = object : TypeToken<List<Folder>>() {}.type
    val folder: List<Folder> = Gson().fromJson(json, listType)

    // Filter unique lessons based on lessonId
    return folder.distinctBy { it.folderId }
}



fun printJsonFlashcards(context: Context){

    val data = loadJsonData(context)

    data.forEach { it ->
        Log.i("json",it.toString())
    }

}

fun printJsonLessons(context: Context){

    val data = loadLessonsFromJsonData(context)

    data.forEach { it ->
        Log.i("json",it.toString())
    }

}
fun printJsonFolder(context: Context){

    val data = loadFolderFromJsonData(context)

    data.forEach { it ->
        Log.i("json",it.toString())
    }

}