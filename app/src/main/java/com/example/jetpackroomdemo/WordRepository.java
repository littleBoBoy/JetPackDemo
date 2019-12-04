package com.example.jetpackroomdemo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

class WordRepository {
    private LiveData<List<Word>> allWordsLive;
    private WordDao wordDao;

    WordRepository(Context context) {
        wordDao = WordDatabase.getInstance(context.getApplicationContext()).getWordDao();
        allWordsLive = wordDao.getAllWords();
    }

    LiveData<List<Word>> getAllWordsLive() {
        return allWordsLive;
    }

    void insertWords(Word... words) {
        new InsertAsyncTask(wordDao).execute(words);
    }

    void updateWords(Word... words) {
        new UpdateAsyncTask(wordDao).execute(words);
    }

    void deleteWords(Word... words) {
        new DeleteAsyncTask(wordDao).execute(words);
    }

    void deleteAllWords() {
        new DeleteAllAsyncTask(wordDao).execute();
    }

    LiveData<List<Word>> findWordsWithPatten(String patten) {
        return wordDao.findWordsWithPatten("%" + patten + "%");
    }

    static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao wordDao;

        InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);

            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao wordDao;

        UpdateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.updataWord(words);

            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao wordDao;

        DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWord(words);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private WordDao wordDao;

        DeleteAllAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }
}
