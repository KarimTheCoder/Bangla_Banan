package com.google.mlkit.samples.vision.digitalink.data.repo;


import android.app.Application;

import androidx.paging.DataSource;

import com.google.mlkit.samples.vision.digitalink.data.room.AppDatabase;
import com.google.mlkit.samples.vision.digitalink.data.room.Word;
import com.google.mlkit.samples.vision.digitalink.data.room.WordDao;

public class Repository {

    private final WordDao dao;
    public Repository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        dao = db.wordDao();
    }

    public void updateTaskName(Word word, String name){
        word.setWord(name);
        AppDatabase.executor.execute(()-> dao.update(word));

    }
    public void deleteTask(Word word){
        AppDatabase.executor.execute(()-> dao.delete(word));
    }

    public void addTask(Word word){
        AppDatabase.executor.execute(()-> dao.addTask(word));
    }


    public DataSource.Factory<Integer, Word> getItemsByListId(int listId){
        return dao.getItemsByListId(listId);
    }
}
