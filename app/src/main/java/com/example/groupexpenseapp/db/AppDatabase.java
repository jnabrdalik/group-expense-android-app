package com.example.groupexpenseapp.db;


import android.content.Context;
import android.provider.ContactsContract;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Group.class, Expense.class, Person.class, ExpensePersonCrossRef.class}, version = 1, exportSchema = true)
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

                for (String groupName : DataGenerator.getGroupNames()) {
                    Group group = new Group(groupName, DataGenerator.getRandomDateTime());
                    int groupId = (int) groupDao.insert(group);

                    ArrayList<Integer> peopleIds = new ArrayList<>();
                    for (String personName : DataGenerator.getRandomNames()) {
                        Person person = new Person(personName, groupId);
                        int personId = (int) personDao.insert(person);
                        peopleIds.add(personId);
                    }

                    for (String expenseDescription : DataGenerator.getRandomExpenseDescriptions()) {
                        Expense expense = new Expense(
                                DataGenerator.getRandomAmount(),
                                expenseDescription,
                                DataGenerator.getRandomDateTime(),
                                groupId,
                                DataGenerator.selectRandomId(peopleIds));
                        int expenseId = (int) expenseDao.insert(expense);

                        List<Integer> owingPeople = DataGenerator.selectRandomIds(peopleIds);
                        for (int personId : owingPeople)
                            expenseDao.insertPersonOwing(expenseId, personId);
                    }
                }
            });
        }
    };

}
