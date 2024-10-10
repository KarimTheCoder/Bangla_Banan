package model;

public class Word {

    private String word;
    private boolean starred;

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
