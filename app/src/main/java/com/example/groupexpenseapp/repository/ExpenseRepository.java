package com.example.groupexpenseapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.AppDatabase;
import com.example.groupexpenseapp.db.dao.ExpenseDao;
import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseAndPayer;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.reactivex.Single;

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
    private LiveData<List<ExpenseAndPayer>> groupExpenses;
    private long currentGroupId;

    private ExpenseRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        expenseDao = database.expenseDao();
    }

    public Single<Long> addExpense(Expense expense) {
        return Single.fromFuture(
                AppDatabase.EXECUTOR_SERVICE.submit(
                        () -> expenseDao.insert(expense)
                )
        );
    }

    public void updateOrInsertExpenseWithPeopleInvolved(Expense expense, Set<Integer> selectedIds, Set<Integer> previouslySelectedIds) {
        AppDatabase.EXECUTOR_SERVICE.execute(() -> {
            // TODO
            expenseDao.updateOrInsertExpense(expense, selectedIds, previouslySelectedIds);
        });
    }

    public LiveData<ExpenseAndPayer> getExpenseAndPayer(long expenseId) {
        return expenseDao.getExpenseAndPayer(expenseId);
    }

    public LiveData<List<ExpenseAndPayer>> getExpensesAndPayers(long groupId) {
        if (currentGroupId == groupId)
            return groupExpenses;

        currentGroupId = groupId;
        groupExpenses = expenseDao.getExpensesAndPayersFromGroupMostExpensiveFirst(groupId);

        return groupExpenses;
    }

}
