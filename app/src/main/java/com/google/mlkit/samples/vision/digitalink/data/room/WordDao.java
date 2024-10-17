package com.google.mlkit.samples.vision.digitalink.data.room;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WordDao {
    @Insert
    void addTask(Word word);
    @Update
    void update(Word word);
    @Delete
    void delete(Word word);

    @Query("SELECT * FROM list_word WHERE id = :listId")
    DataSource.Factory<Integer, Word> getItemsByListId(int listId);
}
