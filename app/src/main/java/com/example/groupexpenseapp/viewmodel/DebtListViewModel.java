package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.debt.Debt;
import com.example.groupexpenseapp.repository.ExpenseRepository;

import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.reactivex.Single;

public class DebtListViewModel extends AndroidViewModel {
    private long groupId;
    private ExpenseRepository repository;
    private LiveData<List<Debt>> debts;

    public DebtListViewModel(@NonNull Application application, long groupId) {
        super(application);

        this.groupId = groupId;
        repository = ExpenseRepository.getInstance(application);
        debts = repository.getDebts(groupId);
    }

    public LiveData<List<Debt>> getDebts() {
        return debts;
    }

    public Single<Long> removeDebt(Debt debt) {
        int groupId = debt.getCreditor().getGroupId();
        Expense expense = new Expense(debt.getAmount(), getApplication().getResources().getString(R.string.payment), OffsetDateTime.now(), LocalDate.now(), groupId, debt.getDebtor().getId());
        Set<Integer> set = Collections.singleton(debt.getCreditor().getId());
        return repository.updateOrInsertExpenseWithPeopleInvolved(expense, set, Collections.EMPTY_SET);
    }

    public LiveData<Boolean> isExpenseListEmpty() {
        return Transformations.map(repository.getExpensesWithPeopleInvolved(groupId), expenses -> expenses.size() == 0);
    }

    public void deleteExpense(long expenseId) {
        repository.deleteExpense(expenseId);
    }
}
