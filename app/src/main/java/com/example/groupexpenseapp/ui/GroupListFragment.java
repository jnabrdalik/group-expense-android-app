package com.example.groupexpenseapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.GroupListFragmentBinding;
import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.viewmodel.GroupListViewModel;

import java.util.List;

public class GroupListFragment extends Fragment {
    private GroupListFragmentBinding binding;
    private GroupAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.group_list_fragment, container, false);
        adapter = new GroupAdapter();
        binding.groups.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final GroupListViewModel viewModel = new ViewModelProvider(this).get(GroupListViewModel.class);

        subscribeUi(viewModel.getAllGroups());
    }

    private void subscribeUi(LiveData<List<Group>> allGroups) {
        allGroups.observe(getViewLifecycleOwner(), groups -> {
            if (groups != null)
                adapter.setGroups(groups);
        });
    }

}
