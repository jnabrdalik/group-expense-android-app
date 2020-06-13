package com.example.groupexpenseapp.db.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ExpenseWithPeopleInvolved {
    @Embedded
    private Expense expense;

    @Relation(parentColumn = "payer_id", entityColumn = "id")
    private Person payer;

    @Relation(
            parentColumn = "id",
            entity = Person.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = ExpensePersonCrossRef.class,
                    parentColumn = "expense_id",
                    entityColumn = "person_id"))
    private List<Person> peopleInvolved;

    public ExpenseWithPeopleInvolved(Expense expense, Person payer, List<Person> peopleInvolved) {
        this.expense = expense;
        this.payer = payer;
        this.peopleInvolved = peopleInvolved;
    }

    public Expense getExpense() {
        return expense;
    }

    public Person getPayer() {
        return payer;
    }

    public List<Person> getPeopleInvolved() {
        return peopleInvolved;
    }

    public String getListOfPeopleInvolvedNames() {
        StringBuilder sb = new StringBuilder();
        for (Person person : peopleInvolved)
            sb.append(person.getName()).append(", ");

        return sb.toString()
                .substring(0, sb.length() - 2);
    }
}
