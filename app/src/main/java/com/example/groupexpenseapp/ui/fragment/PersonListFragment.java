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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.AddPersonDialogBinding;
import com.example.groupexpenseapp.databinding.PersonListFragmentBinding;
import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.ui.adapter.PersonAdapter;
import com.example.groupexpenseapp.viewmodel.PersonListViewModel;
import com.example.groupexpenseapp.viewmodel.factory.PersonListViewModelFactory;

import java.util.List;

public class PersonListFragment extends Fragment {
    private PersonListFragmentBinding binding;
    private PersonListViewModel viewModel;
    private PersonAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.person_list_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        long groupId = requireArguments().getLong("GROUP_ID");
        PersonListViewModelFactory factory = new PersonListViewModelFactory(
                requireActivity().getApplication(), groupId);

        viewModel = new ViewModelProvider(this, factory)
                .get(PersonListViewModel.class);

        adapter = new PersonAdapter();
        binding.people.setAdapter(adapter);
        binding.people.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));
        binding.addGroupButton.setOnClickListener(v -> displayAddPersonDialog());

        subscribeUi(viewModel.getPeople());
    }

    private void displayAddPersonDialog() {
        AddPersonDialogBinding binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.add_person_dialog, null, false);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .setTitle("Dodaj osobę")
                .setPositiveButton("Dodaj", null)
                .setNegativeButton("Anuluj", null)
                .create();

        EditText input = binding.newPersonName;

        dialog.setOnShowListener(d -> {
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);

            // show keyboard
            if (input.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }

            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = editable.toString().trim();
                    if (text.isEmpty() || viewModel.nameExists(text))
                        positiveButton.setEnabled(false);
                    else
                        positiveButton.setEnabled(true);
                }
            });

            positiveButton.setOnClickListener(v -> {
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                String personName = input.getText().toString().trim();
                viewModel.addPerson(personName);
                d.dismiss();
            });
        });

        dialog.show();
    }

    private void subscribeUi(LiveData<List<Person>> allPersons) {
        allPersons.observe(getViewLifecycleOwner(), persons -> {
            if (persons != null)
                adapter.submitList(persons);
        });
    }
}
