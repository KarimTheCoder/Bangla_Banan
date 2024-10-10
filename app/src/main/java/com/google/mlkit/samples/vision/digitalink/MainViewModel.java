package com.google.mlkit.samples.vision.digitalink;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Word;

public class MainViewModel extends ViewModel {

    public static final String BN_Language = "bn";
    public static final String TAG = MainViewModel.class.getSimpleName();

    private String writtenWord;
    private String randoWord = "মানুষ";

    List<Word> wordList = new ArrayList<>();

    private MutableLiveData<Word> _currentWordLiveData = new MutableLiveData<>();
    public LiveData<Word> currentWord = _currentWordLiveData;
    private int currentIndex = 0;



    public void initWords(){

        wordList.add(new Word("আমি"));
        wordList.add(new Word("তুমি"));
        wordList.add(new Word("সে"));
        wordList.add(new Word("আমরা"));
        wordList.add(new Word("তোমরা"));
        wordList.add(new Word("তারা"));
        wordList.add(new Word("যে"));
        wordList.add(new Word("যারা"));
        wordList.add(new Word("যিনি"));
        wordList.add(new Word("যার"));
        wordList.add(new Word("যাদের"));

    }

    public String matchWord(){

        // Normalize both strings
        String normalizedString1 = Normalizer.normalize(writtenWord, Normalizer.Form.NFC);
        String normalizedString2 = Normalizer.normalize(Objects.requireNonNull(_currentWordLiveData.getValue()).getWord(), Normalizer.Form.NFC);

        // Use equals() to compare normalized strings
        if (normalizedString1.equals(normalizedString2)) {
            next();
            return "Strings are equal.";

        } else {
            return "Strings are not equal.";
        }



    }

    public String getWrittenWord(){

        return writtenWord;
    }


    public void setWrittenWord(String writtenWord) {
        this.writtenWord = writtenWord;
    }


    public void startSpellTest(){

        if( nextWordExists(currentIndex)){

            Word nextWord = getNextWord(currentIndex);
            if (nextWord != null) {
                updateCurrentWord(nextWord);
            }else {

                Log.w(TAG, "getNextWord returned null");

            }
        }

    }

    private void updateCurrentWord(Word word){
        _currentWordLiveData.setValue(word);
    }

    private boolean nextWordExists(int index){
        return index < wordList.size();
    }

    public void next(){

        currentIndex++;
        if(nextWordExists(currentIndex)){
            Word nextWord = getNextWord(currentIndex);
            if (nextWord != null) {
                updateCurrentWord(nextWord);
            }else {

                Log.w(TAG, "getNextWord returned null");

            }

        }else {
            Log.w(TAG, "You have gone through all the words!");


        }


    }

    private Word getNextWord( int index){

        if( nextWordExists(index)){

            return wordList.get(index);
        }else {

            // Handle null gracefully
            return null;
        }
    }

}
