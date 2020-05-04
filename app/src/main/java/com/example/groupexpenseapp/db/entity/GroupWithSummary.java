package com.example.groupexpenseapp.db.entity;

import androidx.room.Embedded;

public class GroupWithSummary {
    @Embedded
    private Group group;
    private double expenseSum;
    private int personCount;

    public GroupWithSummary(Group group, double expenseSum, int personCount) {
        this.group = group;
        this.expenseSum = expenseSum;
        this.personCount = personCount;
    }

    public Group getGroup() {
        return group;
    }

    public double getExpenseSum() {
        return expenseSum;
    }

    public int getPersonCount() {
        return personCount;
    }
}
