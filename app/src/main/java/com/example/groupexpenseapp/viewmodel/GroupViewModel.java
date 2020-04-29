package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.repository.GroupRepository;

public class GroupViewModel extends AndroidViewModel {
    private GroupRepository repository;
    private int groupId;
    private LiveData<Group> group;

    public GroupViewModel(@NonNull Application application) {
        super(application);

        repository = GroupRepository.getInstance(application);
        group = repository.getGroup(groupId);
    }


}
