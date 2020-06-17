package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.repository.PersonRepository;

import java.util.List;

import io.reactivex.Single;

public class PersonListViewModel extends AndroidViewModel {
    private LiveData<List<Person>> people;
    private PersonRepository repository;
    private long groupId;

    public PersonListViewModel(@NonNull Application application, long groupId) {
        super(application);

        this.groupId = groupId;
        repository = PersonRepository.getInstance(application);
        people = repository.getPeopleFromGroup(groupId);
    }


    public LiveData<List<Person>> getPeople() {
        return people;
    }

    public boolean nameExists(String personName) {
        if (people != null)
            for (Person person : people.getValue()) {
                if (person.getName().equals(personName))
                    return true;
            }

        return false;
    }

    public void addPerson(String personName) {
        Person person = new Person(personName, (int) groupId);
        repository.updateOrInsertPerson(person);
    }

    public void updatePerson(Person person, String name) {
        person.setName(name);
        repository.updateOrInsertPerson(person);
    }

    public Single<Boolean> deletePerson(Person person) {
        return repository.deletePerson(person);
    }
}
