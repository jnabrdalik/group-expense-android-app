package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.repository.GroupRepository;

import java.util.List;

public class GroupListViewModel extends AndroidViewModel {
    private GroupRepository repository;
    private LiveData<List<Group>> allGroups;

    public GroupListViewModel(@NonNull Application application) {
        super(application);

        repository = GroupRepository.getInstance(application);
        allGroups = repository.getAllGroups();
    }

    public LiveData<List<Group>> getAllGroups() {
        return allGroups;
    }

    public void insertGroup(Group group) {
        repository.insertGroup(group);
    }

}
