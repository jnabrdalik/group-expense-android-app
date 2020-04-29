package com.example.groupexpenseapp.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.groupexpenseapp.db.dao.GroupDao;
import com.example.groupexpenseapp.db.entity.Group;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Group.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "group-expense-app-db";
    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService dbWriteExecutor = Executors.newFixedThreadPool(3);
    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                            .addCallback(populateDb)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract GroupDao groupDao();

    private static RoomDatabase.Callback populateDb = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            dbWriteExecutor.execute(() -> {
                GroupDao groupDao = INSTANCE.groupDao();
                groupDao.deleteAll();

                for (int i = 0; i < 10; i++) {
                    Group group = new Group("Nazwa grupy " + (i+1),
                            "Opis grupy " + (i+1));
                    groupDao.insert(group);
                }

            });
        }
    };

}
