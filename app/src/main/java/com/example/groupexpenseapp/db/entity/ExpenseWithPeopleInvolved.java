package com.example.groupexpenseapp.db.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ExpenseWithPeopleInvolved {
    @Embedded
    private Expense expense;

    @Relation(
            parentColumn = "expense_id",
            entityColumn = "person_id",
            associateBy = @Junction(ExpensePersonCrossRef.class))
    private List<Person> peopleInvolved;

    public ExpenseWithPeopleInvolved(Expense expense, List<Person> peopleInvolved) {
        this.expense = expense;
        this.peopleInvolved = peopleInvolved;
    }

    public Expense getExpense() {
        return expense;
    }

    public List<Person> getPeopleInvolved() {
        return peopleInvolved;
    }
}
