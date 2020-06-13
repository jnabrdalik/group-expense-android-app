package com.example.groupexpenseapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.groupexpenseapp.db.AppDatabase;
import com.example.groupexpenseapp.db.dao.ExpenseDao;
import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseWithPeopleInvolved;
import com.example.groupexpenseapp.debt.Debt;
import com.example.groupexpenseapp.debt.DebtUtil;

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
    private LiveData<List<ExpenseWithPeopleInvolved>> groupExpenses;
    private long currentGroupId;

    private ExpenseRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        expenseDao = database.expenseDao();
    }

    public Single<Long> addExpense(Expense expense) {
        return Single.fromFuture(
                AppDatabase.executorService.submit(
                        () -> expenseDao.insert(expense)
                )
        );
    }

    public void updateOrInsertExpenseWithPeopleInvolved(Expense expense, Set<Integer> selectedIds, Set<Integer> previouslySelectedIds) {
        AppDatabase.executorService.execute(() -> {
            // TODO
            expenseDao.updateOrInsertExpense(expense, selectedIds, previouslySelectedIds);
        });
    }

    public LiveData<ExpenseWithPeopleInvolved> getExpenseWithPeopleInvolved(long expenseId) {
        return expenseDao.getExpenseWithPeopleInvolved(expenseId);
    }

    public LiveData<List<ExpenseWithPeopleInvolved>> getExpensesWithPeopleInvolved(long groupId) {
        if (currentGroupId == groupId)
            return groupExpenses;

        currentGroupId = groupId;
        groupExpenses = expenseDao.getExpensesWithPeopleInvolvedFromGroupMostExpensiveFirst(groupId);

        return groupExpenses;
    }

    public void deleteExpense(Expense expense) {
        AppDatabase.executorService.execute(() -> expenseDao.delete(expense));
    }

    public LiveData<List<Debt>> getDebts(long groupId) {
        return Transformations.switchMap(expenseDao.getExpensesAndPeopleInvolved(groupId), next -> {
            MutableLiveData<List<Debt>> debts = new MutableLiveData<>();
            AppDatabase.executorService.execute(() -> {
                DebtUtil resolver = new DebtUtil(next);
                debts.postValue(resolver.getDebts());
            });
            return debts;
        });

//        MutableLiveData<List<Debt>> debts = new MutableLiveData<>();
//
//        AppDatabase.executorService.execute(() -> {
//            DebtResolver resolver = new DebtResolver(expenseDao.getExpensesAndPeopleInvolvedSync(groupId));
//            debts.postValue(resolver.getDebts());
//        });
//
//        return debts;
    }


}
