package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.repository.PersonRepository;

import java.util.List;

public class ExpenseDetailsViewModel extends AndroidViewModel {
    private PersonRepository repository;
    private LiveData<List<Person>> peopleToSelect;

    public ExpenseDetailsViewModel(@NonNull Application application, long expenseId) {
        super(application);

        repository = PersonRepository.getInstance(application);
        //peopleToSelect = repository.getPeopleInvolvedInExpense(expenseId);
    }

//    public LiveData<List<Person>> getPeopleToSelect() {
//    }
}
