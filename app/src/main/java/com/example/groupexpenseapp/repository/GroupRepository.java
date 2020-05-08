package com.example.groupexpenseapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.AppDatabase;
import com.example.groupexpenseapp.db.dao.GroupDao;
import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.GroupWithSummary;

import org.threeten.bp.OffsetDateTime;

import java.util.List;

public class GroupRepository {
    private static GroupRepository INSTANCE;

    public static GroupRepository getInstance(Application application) {
         if (INSTANCE == null) {
            synchronized (GroupRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GroupRepository(application);
                }
            }
         }

         return INSTANCE;
    }

    private GroupDao groupDao;
    private LiveData<List<Group>> allGroups;
    private LiveData<List<GroupWithSummary>> groupsWithSummary;

    private GroupRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        groupDao = database.groupDao();
        allGroups = groupDao.getGroupsNewestFirst();
        groupsWithSummary = groupDao.getGroupsWithSummaryNewestFirst();
    }

    public LiveData<List<GroupWithSummary>> getGroupsWithSummary() {
        return groupsWithSummary;
    }

    public LiveData<Group> getGroup(long groupId) {
        return groupDao.getGroup(groupId);
    }

    public LiveData<List<Group>> getAllGroups() {
        return allGroups;
    }

//    public Single<Long> insertGroup(Group group) {
//        return groupDao.insert(group);
//        //AppDatabase.dbWriteExecutor.execute(() -> groupDao.insert(group));
//    }

    public void addGroup(String name) {
        AppDatabase.dbWriteExecutor.execute(() -> {
            Group group = new Group(name, OffsetDateTime.now());
            groupDao.insert(group);
        });
    }

    public void deleteGroup(Group group) {
        AppDatabase.dbWriteExecutor.execute(() -> groupDao.delete(group));
    }





}
