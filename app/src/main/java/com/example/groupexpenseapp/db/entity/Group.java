package com.example.groupexpenseapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

@Entity(tableName = "groups")
public class Group {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    @ColumnInfo(name = "time_created")
    private OffsetDateTime timeCreated;

    public Group(String name, OffsetDateTime timeCreated) {
        this.name = name;
        this.timeCreated = timeCreated;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormattedTimeCreated() {
        return timeCreated.format(formatter);
    }

}
