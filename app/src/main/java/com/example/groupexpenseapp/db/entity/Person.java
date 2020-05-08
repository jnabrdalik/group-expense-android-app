package com.example.groupexpenseapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "persons",
        foreignKeys =
        @ForeignKey(entity = Group.class,
                    parentColumns = "id",
                    childColumns = "group_id",
                    onDelete = ForeignKey.CASCADE))
public class Person {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;


    @ColumnInfo(name = "group_id")
    private int groupId;

    public Person(String name, int groupId) {
        this.name = name;
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }
}
