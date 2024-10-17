package com.google.mlkit.samples.vision.digitalink.data.room;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Word.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {


    public abstract WordDao wordDao();

    public static volatile AppDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context){

        if(INSTANCE == null){
            synchronized (AppDatabase.class) {
                if(INSTANCE == null){

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database").build();

                }
            }
        }
        return INSTANCE;
    }





}
