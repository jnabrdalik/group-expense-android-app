package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.repository.ExpenseRepository;
import com.example.groupexpenseapp.repository.PersonRepository;

import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class AddExpenseViewModel extends AndroidViewModel {
    public DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

    private LiveData<List<Person>> people;

    private ExpenseRepository expenseRepository;
    public String description = "";
    public String amountStr = "";
    public String date = LocalDate.now().format(formatter);
    public String payerName = "";
    private int groupId;

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

    public void save() {
        int payerId = findPersonWithName(payerName).getId();
        double amount = Double.parseDouble(amountStr);
        OffsetDateTime timeAdded = OffsetDateTime.now();
        LocalDate date = LocalDate.parse(this.date, formatter);
        Expense expense = new Expense(amount, description, timeAdded, date, groupId, payerId);

        expenseRepository.updateOrInsertExpenseWithPeopleInvolved(expense, selectedItemIds, Collections.EMPTY_SET);
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
