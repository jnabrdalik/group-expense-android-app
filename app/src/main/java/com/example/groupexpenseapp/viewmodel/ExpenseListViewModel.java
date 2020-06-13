package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseWithPeopleInvolved;
import com.example.groupexpenseapp.repository.ExpenseRepository;

import java.util.List;

public class ExpenseListViewModel extends AndroidViewModel {
    private LiveData<List<ExpenseWithPeopleInvolved>> groupExpenses;
    private ExpenseRepository repository;

    public ExpenseListViewModel(@NonNull Application application, long groupId) {
        super(application);

        repository = ExpenseRepository.getInstance(application);
        groupExpenses = repository.getExpensesWithPeopleInvolved(groupId);
    }

    public LiveData<List<ExpenseWithPeopleInvolved>> getGroupExpenses() {
        return groupExpenses;
    }

    public void deleteExpense(Expense expense) {
        repository.deleteExpense(expense);
    }

}
