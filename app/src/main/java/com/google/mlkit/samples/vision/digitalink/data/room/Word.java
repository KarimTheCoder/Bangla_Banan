package com.google.mlkit.samples.vision.digitalink.data.room;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "list_word")
public class Word {

    @PrimaryKey(autoGenerate = true)
     int id;
     String word;
     boolean starred;

    public Word(String word) {
        this.word = word;
    }



    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
