package com.example.groupexpenseapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.GroupWithSummary;
import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.repository.GroupRepository;
import com.example.groupexpenseapp.repository.PersonRepository;

import org.threeten.bp.OffsetDateTime;

import java.util.List;

import io.reactivex.Single;

public class GroupListViewModel extends AndroidViewModel {
    private GroupRepository groupRepository;
    private PersonRepository personRepository;
    private LiveData<List<GroupWithSummary>> allGroupsWithSummary;

    public GroupListViewModel(@NonNull Application application) {
        super(application);

        groupRepository = GroupRepository.getInstance(application);
        personRepository = PersonRepository.getInstance(application);
        
        allGroupsWithSummary = groupRepository.getGroupsWithSummary();
    }

    public Single<Long> addGroup(String name) {
        Group group = new Group(name, OffsetDateTime.now());
        return groupRepository.insertGroup(group);
    }

    public void deleteGroup(Group group) {
        groupRepository.deleteGroup(group);
    }

    public LiveData<List<GroupWithSummary>> getAllGroupsWithSummary() {
        return allGroupsWithSummary;
    }

    public void addMeToGroup(long groupId) {
        String me = getApplication().getResources().getString(R.string.me);

        personRepository.updateOrInsertPerson(new Person(me, (int) groupId));
    }

    public void updateGroup(Group group, String newGroupName) {
        group.setName(newGroupName);
        groupRepository.updateGroup(group);
    }
}
