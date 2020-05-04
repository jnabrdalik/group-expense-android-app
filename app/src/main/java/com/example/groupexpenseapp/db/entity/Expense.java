package com.example.groupexpenseapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.threeten.bp.OffsetDateTime;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double amount;
    private String description;

    @ColumnInfo(name = "time_added")
    private OffsetDateTime timeAdded;

    @ForeignKey(entity = Group.class,
                parentColumns = "id",
                childColumns = "group_id",
                onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name = "group_id")
    private int groupId;

    @ForeignKey(entity = Person.class,
                parentColumns = "id",
                childColumns = "payer_id")
    @ColumnInfo(name = "payer_id")
    private int payerId;

    public Expense(double amount, String description, OffsetDateTime timeAdded, int groupId, int payerId) {
        this.amount = amount;
        this.description = description;
        this.timeAdded = timeAdded;
        this.groupId = groupId;
        this.payerId = payerId;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getTimeAdded() {
        return timeAdded;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
