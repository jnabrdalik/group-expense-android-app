package com.example.groupexpenseapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.AppDatabase;
import com.example.groupexpenseapp.db.dao.GroupDao;
import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.GroupWithSummary;

import java.util.List;

import io.reactivex.Single;

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
    private LiveData<List<GroupWithSummary>> groupsWithSummary;

    private GroupRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        groupDao = database.groupDao();
        groupsWithSummary = groupDao.getGroupsWithSummaryNewestFirst();
    }

    public LiveData<List<GroupWithSummary>> getGroupsWithSummary() {
        return groupsWithSummary;
    }

    public LiveData<Group> getGroup(long groupId) {
        return groupDao.getGroup(groupId);
    }

    public Single<Long> insertGroup(Group group) {
        return Single.fromFuture(
                AppDatabase.executorService.submit(
                        () -> groupDao.insert(group)
                )
        );
    }

    public void updateGroup(Group group) {
        AppDatabase.executorService.execute(() -> groupDao.update(group));
    }

    public void deleteGroup(Group group) {
        AppDatabase.executorService.execute(() -> groupDao.delete(group));
    }
}
