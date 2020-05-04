package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.repository.GroupRepository;

public class GroupViewModel extends AndroidViewModel {
    private GroupRepository repository;
    private long groupId;
    private LiveData<Group> group;

    public GroupViewModel(@NonNull Application application, long groupId) {
        super(application);

        repository = GroupRepository.getInstance(application);
        this.groupId = groupId;
        group = repository.getGroup(groupId);
    }

    public LiveData<Group> getGroup() {
        return group;
    }

}
