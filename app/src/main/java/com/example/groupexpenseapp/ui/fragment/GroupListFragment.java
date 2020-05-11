package com.example.groupexpenseapp.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.AddGroupDialogBinding;
import com.example.groupexpenseapp.databinding.GroupListFragmentBinding;
import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.GroupWithSummary;
import com.example.groupexpenseapp.ui.adapter.GroupAdapter;
import com.example.groupexpenseapp.viewmodel.GroupListViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class GroupListFragment extends Fragment {
    GroupListViewModel viewModel;
    private GroupListFragmentBinding binding;
    private GroupAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.group_list_fragment, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(GroupListViewModel.class);
        adapter = new GroupAdapter(group -> displayGroupDeleteWarningDialog(group.getGroup()));

        binding.addGroupButton.setOnClickListener(v -> displayCreateGroupDialog());

        subscribeUi(viewModel.getAllGroupsWithSummary());
        binding.groups.setAdapter(adapter);
    }

    private void subscribeUi(LiveData<List<GroupWithSummary>> allGroups) {
        allGroups.observe(getViewLifecycleOwner(), groups -> {
            if (groups != null)
                adapter.setGroupList(groups);
        });
    }

    private void displayGroupDeleteWarningDialog(Group group) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Usuń grupę")
                .setMessage("Czy na pewno chcesz usunąć tę grupę? Tej operacji nie można cofnąć.")
                .setPositiveButton("Tak", (dialog, which) -> viewModel.deleteGroup(group))
                .setNegativeButton("Anuluj", null)
                .show();
    }

    private void displayCreateGroupDialog() {
        AddGroupDialogBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.add_group_dialog, null, false);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .setTitle("Dodaj grupę")
                .setPositiveButton("Dodaj", null)
                .setNegativeButton("Anuluj", null)
                .create();


        EditText input = binding.newGroupName;

        dialog.setOnShowListener(d -> {
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);
            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().trim().isEmpty())
                        positiveButton.setEnabled(false);
                    else
                        positiveButton.setEnabled(true);
                }
            });

            positiveButton.setOnClickListener(v -> {
                String newGroupName = input.getText().toString().trim();
                viewModel.addGroup(newGroupName);
                d.dismiss();
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) this.binding.groups.getLayoutManager();
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
                //poczekaj aż się utworzy i wejdź w grupę
            });
        });

        dialog.show();
    }

}
