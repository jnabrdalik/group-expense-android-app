package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.debt.Debt;
import com.example.groupexpenseapp.repository.ExpenseRepository;

import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Single;

public class DebtListViewModel extends AndroidViewModel {
    private ExpenseRepository repository;
    private LiveData<List<Debt>> debts;
    private Set<Debt> selectedItems = new HashSet<>();

    public DebtListViewModel(@NonNull Application application, long groupId) {
        super(application);

        repository = ExpenseRepository.getInstance(application);
        debts = repository.getDebts(groupId);
    }

    public LiveData<List<Debt>> getDebts() {
        return debts;
    }

    public void resetSelection() {
        selectedItems.clear();
    }

    public Single<Long> removeDebt(Debt debt) {
        int groupId = debt.getCreditor().getGroupId();
        Expense expense = new Expense(debt.getAmount(), getApplication().getResources().getString(R.string.payment), OffsetDateTime.now(), LocalDate.now(), groupId, debt.getDebtor().getId());
        Set<Integer> set = Collections.singleton(debt.getCreditor().getId());
        return repository.updateOrInsertExpenseWithPeopleInvolved(expense, set, Collections.EMPTY_SET);
    }

    public void deleteExpense(long expenseId) {
        repository.deleteExpense(expenseId);
    }
}
