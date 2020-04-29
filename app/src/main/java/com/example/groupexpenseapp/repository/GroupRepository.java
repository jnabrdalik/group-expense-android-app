package com.example.groupexpenseapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.AppDatabase;
import com.example.groupexpenseapp.db.dao.GroupDao;
import com.example.groupexpenseapp.db.entity.Group;

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

    private GroupRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        groupDao = database.groupDao();
        allGroups = groupDao.loadAllGroups();
    }

    public LiveData<Group> getGroup(int groupId) {
        return groupDao.loadGroup(groupId);
    }

    public LiveData<List<Group>> getAllGroups() {
        return allGroups;
    }

    public void insertGroup(Group group) {
        AppDatabase.dbWriteExecutor.execute(() -> groupDao.insert(group));
    }





}
