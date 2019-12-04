package com.example.jetpackroomdemo;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao //database access object
public interface WordDao {
    @Insert
    void insertWords(Word... words);

    @Update
    void updataWord(Word... words);

    @Delete
    void deleteWord(Word... words);

    @Query(value = "DELETE FROM WORD")
    void deleteAllWords();

    @Query(value = "SELECT * FROM WORD ORDER BY ID DESC")
    LiveData<List<Word>> getAllWords();

    @Query(value = "SELECT * FROM WORD WHERE english_name LIKE :patten ORDER BY ID DESC")
    LiveData<List<Word>> findWordsWithPatten(String patten);
}


