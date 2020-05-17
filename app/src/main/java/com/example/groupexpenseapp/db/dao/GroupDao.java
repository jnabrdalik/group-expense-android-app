package com.example.groupexpenseapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.GroupWithSummary;

import java.util.List;

@Dao
public interface GroupDao {
    @Insert
    long insert(Group group);

    @Update
    void update(Group group);

    @Delete
    void delete(Group group);

    @Query("select * from groups where id = :groupId")
    LiveData<Group> getGroup(long groupId);

    @Query("select * from groups order by datetime(time_created) desc")
    LiveData<List<Group>> getGroupsNewestFirst();

    @Query("select g.*, (select sum(amount) from expenses e where e.group_id = g.id) expenseSum, (select count(p.id) from persons p where p.group_id = g.id) as personCount from groups g order by datetime(g.time_created) desc")
    LiveData<List<GroupWithSummary>> getGroupsWithSummaryNewestFirst();

    @Query("select g.*, (select sum(amount) from expenses e where e.group_id = g.id) expenseSum, (select count(p.id) from persons p where p.group_id = g.id) as personCount from groups g order by name")
    LiveData<List<GroupWithSummary>> getGroupsWithSummaryAlphabetically();

    //only temporary
    @Query("delete from groups")
    void deleteAll();
}
