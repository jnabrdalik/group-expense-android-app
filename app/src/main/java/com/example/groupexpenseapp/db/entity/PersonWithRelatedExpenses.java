package com.example.groupexpenseapp.db.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class PersonWithRelatedExpenses {
    @Embedded
    private Person person;

    @Relation(
            parentColumn = "person_id",
            entityColumn = "expense_id",
            associateBy = @Junction(ExpensePersonCrossRef.class))
    private List<Expense> relatedExpenses;

    public PersonWithRelatedExpenses(Person person, List<Expense> relatedExpenses) {
        this.person = person;
        this.relatedExpenses = relatedExpenses;
    }

    public Person getPerson() {
        return person;
    }

    public List<Expense> getRelatedExpenses() {
        return relatedExpenses;
    }
}
