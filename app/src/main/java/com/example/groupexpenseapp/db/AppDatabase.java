package com.example.groupexpenseapp.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.groupexpenseapp.db.converter.DateConverter;
import com.example.groupexpenseapp.db.dao.ExpenseDao;
import com.example.groupexpenseapp.db.dao.GroupDao;
import com.example.groupexpenseapp.db.dao.PersonDao;
import com.example.groupexpenseapp.db.entity.*;

import org.threeten.bp.OffsetDateTime;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Group.class, Expense.class, Person.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
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
                            .fallbackToDestructiveMigration()
                            .addCallback(populateDb)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract GroupDao groupDao();
    public abstract ExpenseDao expenseDao();
    public abstract PersonDao personDao();

    private static RoomDatabase.Callback populateDb = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            dbWriteExecutor.execute(() -> {
                GroupDao groupDao = INSTANCE.groupDao();
                ExpenseDao expenseDao = INSTANCE.expenseDao();
                PersonDao personDao = INSTANCE.personDao();
                groupDao.deleteAll();
                personDao.deleteAll();

                for (int i = 0; i < 10; i++) {
                    Group group = new Group("Nazwa grupy " + (i+1), OffsetDateTime.now());
                    long groupId = groupDao.insert(group);


                    for (int j = 0; j < 15; j++) {
                        Person person = new Person("Osoba nr " + (j+1), (int) groupId);
                        long personId = personDao.insertPerson(person);

                        double amount = Math.random() * 1000;
                        Expense expense = new Expense(amount, "Wydatek nr " + j, OffsetDateTime.now(), (int) groupId, (int) personId);
                        expenseDao.insertExpense(expense);
                    }
                }

            });
        }
    };

}
