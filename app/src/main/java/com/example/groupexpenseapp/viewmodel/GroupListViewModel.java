package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.GroupWithSummary;
import com.example.groupexpenseapp.repository.GroupRepository;

import java.util.List;

public class GroupListViewModel extends AndroidViewModel {
    private GroupRepository repository;
    private LiveData<List<Group>> allGroups;
    private LiveData<List<GroupWithSummary>> allGroupsWithSummary;

    public GroupListViewModel(@NonNull Application application) {
        super(application);

        repository = GroupRepository.getInstance(application);
        allGroups = repository.getAllGroups();
        allGroupsWithSummary = repository.getGroupsWithSummary();
    }

    public LiveData<List<Group>> getGroups() {
        return allGroups;
    }

    public void addGroup(String name) {
        repository.addGroup(name);
    }

    public void deleteGroup(Group group) {
        repository.deleteGroup(group);
    }

    public LiveData<List<GroupWithSummary>> getAllGroupsWithSummary() {
        return allGroupsWithSummary;
    }
}
