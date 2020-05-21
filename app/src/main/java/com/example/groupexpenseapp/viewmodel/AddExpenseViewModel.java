package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseAndPayer;
import com.example.groupexpenseapp.db.entity.ExpenseWithPeopleInvolved;
import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.repository.ExpenseRepository;
import com.example.groupexpenseapp.repository.PersonRepository;

import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddExpenseViewModel extends AndroidViewModel {
    public DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

    private LiveData<List<Person>> people;

    private ExpenseRepository expenseRepository;
    private ExpenseWithPeopleInvolved expenseWithPeopleInvolved;
    public String description = "";
    public String amountStr = "";
    public String date = LocalDate.now().format(formatter);
    public String payerName = "";
    private int groupId;
    private int expenseId;

    private HashSet<Integer> selectedItemIds = new HashSet<>();

    public AddExpenseViewModel(@NonNull Application application, long groupId) {
        super(application);

        this.groupId = (int) groupId;

        PersonRepository repository = PersonRepository.getInstance(application);
        expenseRepository = ExpenseRepository.getInstance(application);
        people = repository.getPeopleFromGroup(groupId);
    }

    public LiveData<List<Person>> getAllPeople() {
        return people;
    }

    public LiveData<ExpenseWithPeopleInvolved> getExpenseWithPeopleInvolved(long expenseId) {
        return expenseRepository.getExpenseWithPeopleInvolved(expenseId);
    }

    public void setExpense(ExpenseWithPeopleInvolved expenseWithPeopleInvolved) {
        this.expenseWithPeopleInvolved = expenseWithPeopleInvolved;
        Expense expense = expenseWithPeopleInvolved.getExpense();
        expenseId = expense.getId();
        description = expense.getDescription();
        amountStr = Double.toString(expense.getAmount());
        date = expense.getDate().format(formatter);
        payerName = expenseWithPeopleInvolved.getPayer().getName();
        for (Person person : expenseWithPeopleInvolved.getPeopleInvolved())
            selectedItemIds.add(person.getId());
    }

    public void save() {
        int payerId = findPersonWithName(payerName).getId();
        double amount = Double.parseDouble(amountStr);
        OffsetDateTime timeAdded = OffsetDateTime.now();
        LocalDate date = LocalDate.parse(this.date, formatter);
        Expense expense = new Expense(amount, description, timeAdded, date, groupId, payerId);

        Set<Integer> previouslySelectedIds = new HashSet<>();
        if (expenseId != 0) {
            expense.setId(expenseId);
            for (Person person : expenseWithPeopleInvolved.getPeopleInvolved())
                previouslySelectedIds.add(person.getId());
        }

        expenseRepository.updateOrInsertExpenseWithPeopleInvolved(expense, selectedItemIds, previouslySelectedIds);
    }

    public Person findPersonWithName(String name) {
        if (people != null)
            for (Person person : people.getValue()) {
                if (person.getName().equals(name))
                    return person;
            }

        return null;
    }

    public HashSet<Integer> getSelectedItemIds() {
        return selectedItemIds;
    }

    public boolean areAllFieldsSet() {
        return !(description.trim().isEmpty() ||
                amountStr.trim().isEmpty() ||
                date.trim().isEmpty() ||
                payerName.trim().isEmpty() ||
                selectedItemIds.isEmpty());
    }

}
