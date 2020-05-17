package com.example.groupexpenseapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.AppDatabase;
import com.example.groupexpenseapp.db.dao.PersonDao;
import com.example.groupexpenseapp.db.entity.Person;

import java.util.List;

public class PersonRepository {
    private static PersonRepository INSTANCE;

    public static PersonRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (PersonRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PersonRepository(application);
                }
            }
        }

        return INSTANCE;
    }

    private PersonDao personDao;
    private LiveData<List<Person>> groupPeople;
    private LiveData<List<Person>> peopleInvolvedInExpense;
    private long currentGroupId;
    private long currentExpenseId;

    private PersonRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        personDao = database.personDao();
    }

    public LiveData<List<Person>> getPeopleFromGroup(long groupId) {
        if (currentGroupId == groupId)
            return groupPeople;

        this.currentGroupId = groupId;
        this.groupPeople = personDao.getPeopleFromGroup(groupId);
        return groupPeople;
    }

    public LiveData<List<Person>> getPeopleInvolvedInExpense(long expenseId) {
        if (currentExpenseId != expenseId) {
            currentExpenseId = expenseId;
            peopleInvolvedInExpense = personDao.getPeopleOwingForExpense(expenseId);
        }

        return peopleInvolvedInExpense;
    }

    public void addPerson(Person person) {
        AppDatabase.EXECUTOR_SERVICE.execute(() -> personDao.insert(person));
    }
}
