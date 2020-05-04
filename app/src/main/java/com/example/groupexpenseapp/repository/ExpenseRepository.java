package com.example.groupexpenseapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.AppDatabase;
import com.example.groupexpenseapp.db.dao.ExpenseDao;
import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseAndPayer;

import java.util.List;

public class ExpenseRepository {
    private static ExpenseRepository INSTANCE;

    public static ExpenseRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ExpenseRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ExpenseRepository(application);
                }
            }
        }

        return INSTANCE;
    }

    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> groupExpenses;
    private long currentGroupId;

    private ExpenseRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        expenseDao = database.expenseDao();
    }

    public LiveData<List<Expense>> getGroupExpenses(long groupId) {
        if (currentGroupId == groupId)
            return groupExpenses;

        this.currentGroupId = groupId;
        this.groupExpenses = expenseDao.getExpensesFromGroupNewestFirst(groupId);
        return groupExpenses;
    }

    public LiveData<List<ExpenseAndPayer>> getExpenseAndPayer(long groupId) {
        return expenseDao.getExpensesAndPayers(groupId);
    }


}
