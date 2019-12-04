package com.example.jetpackroomdemo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word extends BaseObservable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "english_name")
    private String word;

    @ColumnInfo(name = "chinese_meaning")
    private String chineseMeaning;

    @ColumnInfo(name = "chinese_invisiable")
    private boolean chineseInvisiable;

    public Word(String word, String chineseMeaning) {
        this.word = word;
        this.chineseMeaning = chineseMeaning;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getChineseMeaning() {
        return chineseMeaning;
    }

    public void setChineseMeaning(String chineseMeaning) {
        this.chineseMeaning = chineseMeaning;
    }

    @Bindable
    public boolean isChineseInvisiable() {
        return chineseInvisiable;
    }

    public void setChineseInvisiable(boolean chineseInvisiable) {
        this.chineseInvisiable = chineseInvisiable;
        notifyPropertyChanged(com.example.jetpackroomdemo.BR.chineseInvisiable);
    }
}
