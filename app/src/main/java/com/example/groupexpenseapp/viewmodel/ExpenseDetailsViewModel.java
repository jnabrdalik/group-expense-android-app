package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseAndPayer;
import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.repository.ExpenseRepository;
import com.example.groupexpenseapp.repository.PersonRepository;

import java.util.List;

public class ExpenseDetailsViewModel extends AndroidViewModel {
    private ExpenseRepository expenseRepository;
    private PersonRepository personRepository;
    private LiveData<ExpenseAndPayer> expense;
    private LiveData<List<Person>> peopleInvolved;

    public ExpenseDetailsViewModel(@NonNull Application application, long expenseId) {
        super(application);

        expenseRepository = ExpenseRepository.getInstance(application);
        personRepository = PersonRepository.getInstance(application);

        expense = expenseRepository.getExpenseAndPayer(expenseId);
        peopleInvolved = personRepository.getPeopleInvolvedInExpense(expenseId);
    }

    public LiveData<ExpenseAndPayer> getExpenseAndPayer() {
        return expense;
    }

    public LiveData<List<Person>> getPeopleInvolved() {
        return peopleInvolved;
    }

    public void deleteExpense(Expense expense) {
        expenseRepository.deleteExpense(expense);
    }
}
