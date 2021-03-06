package com.example.groupexpenseapp.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.AddGroupDialogBinding;
import com.example.groupexpenseapp.databinding.GroupListFragmentBinding;
import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.GroupWithSummary;
import com.example.groupexpenseapp.ui.adapter.GroupAdapter;
import com.example.groupexpenseapp.viewmodel.GroupListViewModel;
import com.google.android.material.snackbar.Snackbar;

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
        adapter = new GroupAdapter(new GroupAdapter.GroupClickListener() {
            @Override
            public void onDelete(Group group) {
                displayGroupDeleteWarningDialog(group);
            }

            @Override
            public void onEdit(Group group) {
                displayCreateOrEditGroupDialog(group);
            }
        });

        binding.addGroupButton.setOnClickListener(v -> displayCreateOrEditGroupDialog(null));
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewmodel(viewModel);


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
                .setPositiveButton("Usuń", (dialog, which) -> {
                    viewModel.deleteGroup(group);
                    Snackbar.make(requireView(), "Usunięto grupę \"" + group.getName() + "\"", Snackbar.LENGTH_SHORT)
                            .show();
                })
                .setNegativeButton("Anuluj", null)
                .show();
    }

    private void displayCreateOrEditGroupDialog(Group group) {
        AddGroupDialogBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.add_group_dialog, null, false);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .setTitle(group == null ? "Dodaj grupę" : "Edytuj grupę")
                .setPositiveButton("Zapisz", null)
                .setNegativeButton("Anuluj", null)
                .create();

        EditText input = binding.newGroupName;

        dialog.setOnShowListener(d -> {
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            positiveButton.setEnabled(false);

            showKeyboard(input);

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

            if (group != null) {
                String name = group.getName();
                input.setText(name);
                input.setSelection(name.length());
            }

            positiveButton.setOnClickListener(v -> {
                String newGroupName = input.getText().toString().trim();

                if (group == null) {
                    viewModel.addGroup(newGroupName).subscribe(groupId -> {

                        viewModel.addMeToGroup(groupId);

                        NavDirections directions = GroupListFragmentDirections
                                .actionGroupListFragmentToGroupFragment(groupId);
                        NavHostFragment.findNavController(this).navigate(directions);
                    }).dispose();
                }
                else {
                    viewModel.updateGroup(group, newGroupName);
                }

                hideKeyboard(input);
                d.dismiss();
            });

            negativeButton.setOnClickListener(v -> {
                hideKeyboard(input);
                d.dismiss();
            });
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

}
