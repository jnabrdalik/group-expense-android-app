package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.GroupWithSummary;
import com.example.groupexpenseapp.repository.GroupRepository;

import org.threeten.bp.OffsetDateTime;

import java.util.List;

import io.reactivex.Single;

public class GroupListViewModel extends AndroidViewModel {
    private GroupRepository repository;
    private LiveData<List<GroupWithSummary>> allGroupsWithSummary;

    public GroupListViewModel(@NonNull Application application) {
        super(application);

        repository = GroupRepository.getInstance(application);
        allGroupsWithSummary = repository.getGroupsWithSummary();
    }

    public Single<Long> addGroup(String name) {
        Group group = new Group(name, OffsetDateTime.now());
        return repository.addGroup(group);
    }

    public void deleteGroup(Group group) {
        repository.deleteGroup(group);
    }

    public LiveData<List<GroupWithSummary>> getAllGroupsWithSummary() {
        return allGroupsWithSummary;
    }
}
