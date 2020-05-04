package com.example.groupexpenseapp.db.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ExpenseAndPayer {
    @Embedded
    private Expense expense;
    @Relation(parentColumn = "payer_id", entityColumn = "id")
    private Person payer;

    public ExpenseAndPayer(Expense expense, Person payer) {
        this.expense = expense;
        this.payer = payer;
    }

    public Expense getExpense() {
        return expense;
    }

    public Person getPayer() {
        return payer;
    }
}
