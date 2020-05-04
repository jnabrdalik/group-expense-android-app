package com.example.groupexpenseapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.db.entity.PersonWithRelatedExpenses;

import java.util.List;

@Dao
public interface PersonDao {
    @Insert
    long insertPerson(Person person);

    @Query("select * from persons where group_id = :groupId order by name")
    LiveData<List<Person>> getPeopleFromGroup(long groupId);

    @Query("delete from persons")
    void deleteAll();

    @Query("select * from persons where id = :personId")
    LiveData<PersonWithRelatedExpenses> getPersonWithRelatedExpenses(long personId);
}
