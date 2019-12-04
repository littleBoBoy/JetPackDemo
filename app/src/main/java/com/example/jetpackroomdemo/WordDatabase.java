package com.example.jetpackroomdemo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {
    private static WordDatabase instance;

    static WordDatabase getInstance(Context c) {
        if (instance == null) {
            synchronized (WordDatabase.class) {
                if (instance == null)
                    instance = Room.databaseBuilder(c.getApplicationContext(), WordDatabase.class, "word_database")
//                            .allowMainThreadQueries()
//                            数据库迁移
//                            .fallbackToDestructiveMigration()  //销毁原来的直接创建新的
//                            .addMigrations(MIGRATION_3_4)
                            .build();
            }
        }
        return instance;
    }

    public abstract WordDao getWordDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //增加行
            database.execSQL("ALTER TABLE WORD ADD COLUMN show_chinese INTEGER NOT NULL DEFAULT 1");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //修改字段属性  重建表
            database.execSQL(
                    "CREATE TABLE word_new (id INTEGER NOT NULL,"
                            + "english_name TEXT,"
                            + "chinese_meaning TEXT,"
                            + "show_chinese INTEGER NOT NULL DEFAULT 1,"
                            + "PRIMARY KEY(id))");
            database.execSQL("INSERT INTO word_new (id, english_name, chinese_meaning,show_chinese) "
                    + "SELECT id, english_name, chinese_meaning ,1 "
                    + "FROM word");
            database.execSQL("DROP TABLE word");
            database.execSQL("ALTER TABLE word_new RENAME TO word");
        }
    };
}
