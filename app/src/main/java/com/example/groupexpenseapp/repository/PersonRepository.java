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
    private LiveData<List<Person>> groupPersons;
    private long currentGroupId;

    private PersonRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        personDao = database.personDao();
    }

    public LiveData<List<Person>> getGroupPersons(long groupId) {
        if (currentGroupId == groupId)
            return groupPersons;

        this.currentGroupId = groupId;
        this.groupPersons = personDao.getPeopleFromGroup(groupId);
        return groupPersons;
    }
}