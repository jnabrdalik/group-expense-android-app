package com.example.groupexpenseapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.GroupWithSummary;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Group group);

    @Delete
    void delete(Group group);

    @Query("select * from groups order by datetime(time_created) desc")
    LiveData<List<Group>> getAllGroups();

    @Query("select * from groups where id = :groupId")
    LiveData<Group> getGroup(long groupId);

    @Query("select groups.*, (select sum(amount) from expenses where expenses.group_id = groups.id) expenseSum, (select count(persons.id) from persons where persons.group_id = groups.id) as personCount from groups order by datetime(groups.time_created) desc")
    LiveData<List<GroupWithSummary>> getGroupsWithSummary();

    @Query("delete from groups")
    void deleteAll();
}
