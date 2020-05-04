package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.repository.PersonRepository;

import java.util.List;

public class PersonListViewModel extends AndroidViewModel {
    private LiveData<List<Person>> persons;

    public PersonListViewModel(@NonNull Application application, long groupId) {
        super(application);

        PersonRepository repository = PersonRepository.getInstance(application);
        persons = repository.getGroupPersons(groupId);
    }


    public LiveData<List<Person>> getPersons() {
        return persons;
    }
}
