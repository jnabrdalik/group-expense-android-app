package com.example.groupexpenseapp.db.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class PersonWithRelatedExpenses {
    @Embedded
    private Person person;

    @Relation(
            parentColumn = "id",
            entityColumn = "payer_id")
    private List<Expense> expensesPaidFor;

    @Relation(
            parentColumn = "id",
            entity = Expense.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = ExpensePersonCrossRef.class,
                    parentColumn = "person_id",
                    entityColumn = "expense_id"))
    private List<Expense> expensesWhereOwes;

    public PersonWithRelatedExpenses(Person person, List<Expense> expensesPaidFor, List<Expense> expensesWhereOwes) {
        this.person = person;
        this.expensesPaidFor = expensesPaidFor;
        this.expensesWhereOwes = expensesWhereOwes;
    }

    public Person getPerson() {
        return person;
    }

    public List<Expense> getExpensesPaidFor() {
        return expensesPaidFor;
    }

    public List<Expense> getExpensesWhereOwes() {
        return expensesWhereOwes;
    }

}
