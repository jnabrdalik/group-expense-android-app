package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseWithPeopleInvolved;
import com.example.groupexpenseapp.repository.ExpenseRepository;
import com.example.groupexpenseapp.repository.PersonRepository;

import java.util.List;

public class ExpenseListViewModel extends AndroidViewModel {
    private long groupId;
    private LiveData<List<ExpenseWithPeopleInvolved>> groupExpenses;
    private ExpenseRepository expenseRepository;
    private PersonRepository personRepository;

    public ExpenseListViewModel(@NonNull Application application, long groupId) {
        super(application);

        this.groupId = groupId;
        expenseRepository = ExpenseRepository.getInstance(application);
        personRepository = PersonRepository.getInstance(application);
        groupExpenses = expenseRepository.getExpensesWithPeopleInvolved(groupId);
    }

    public LiveData<List<ExpenseWithPeopleInvolved>> getGroupExpenses() {
        return groupExpenses;
    }

    public LiveData<Boolean> moreThanOnePersonInGroup() {
        return Transformations.map(personRepository.getPeopleFromGroup(groupId), list -> list.size() >= 2);
    }

    public void deleteExpense(Expense expense) {
        expenseRepository.deleteExpense(expense);
    }

}
