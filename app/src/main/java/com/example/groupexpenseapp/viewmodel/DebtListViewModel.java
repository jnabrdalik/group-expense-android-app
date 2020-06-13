package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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

    public Set<Debt> getSelectedItems() {
        return selectedItems;
    }

    public void resetSelection() {
        selectedItems.clear();
    }

    public void payDebts() {
        for (Debt debt : selectedItems) {
            // TODO make seperate class (or Expense subclass) for payments
            int groupId = debt.getCreditor().getGroupId();
            Expense expense = new Expense(debt.getAmount(), "SPŁATA", OffsetDateTime.now(), LocalDate.now(), groupId, debt.getDebtor().getId());
            Set<Integer> set = Collections.singleton(debt.getCreditor().getId());
            repository.updateOrInsertExpenseWithPeopleInvolved(expense, set, Collections.EMPTY_SET);
        }
    }
}
