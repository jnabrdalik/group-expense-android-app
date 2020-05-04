package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.entity.ExpenseAndPayer;
import com.example.groupexpenseapp.repository.ExpenseRepository;

import java.util.List;

public class ExpenseListViewModel extends AndroidViewModel {
    private LiveData<List<ExpenseAndPayer>> groupExpenses;

    public ExpenseListViewModel(@NonNull Application application, long groupId) {
        super(application);

        ExpenseRepository repository = ExpenseRepository.getInstance(application);
        groupExpenses = repository.getExpenseAndPayer(groupId);
    }

    public LiveData<List<ExpenseAndPayer>> getGroupExpenses() {
        return groupExpenses;
    }


}
