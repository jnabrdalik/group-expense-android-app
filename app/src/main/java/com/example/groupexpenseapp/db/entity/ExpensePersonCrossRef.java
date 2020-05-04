package com.example.groupexpenseapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"expense_id", "person_id"})
public class ExpensePersonCrossRef {
    @ForeignKey(entity = Expense.class,
                parentColumns = "id",
                childColumns = "expense_id",
                onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name = "expense_id")
    private int expenseId;

    @ForeignKey(entity = Expense.class,
                parentColumns = "id",
                childColumns = "expense_id")
    @ColumnInfo(name = "person_id")
    private int personId;

    public ExpensePersonCrossRef(int expenseId, int personId) {
        this.expenseId = expenseId;
        this.personId = personId;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public int getPersonId() {
        return personId;
    }
}
