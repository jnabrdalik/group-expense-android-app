package com.example.groupexpenseapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.groupexpenseapp.db.entity.Group;

import java.util.List;

@Dao
public interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Group group);

    @Query("select * from groups")
    LiveData<List<Group>> loadAllGroups();

    @Query("select * from groups where id = :groupId")
    LiveData<Group> loadGroup(int groupId);

    @Query("delete from groups")
    void deleteAll();
}
