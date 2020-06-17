package com.example.groupexpenseapp.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.AddExpenseFragmentBinding;
import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.ui.adapter.PersonInvolvedChoiceAdapter;
import com.example.groupexpenseapp.viewmodel.AddExpenseViewModel;
import com.example.groupexpenseapp.viewmodel.factory.AddExpenseViewModelFactory;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;


public class AddExpenseFragment extends Fragment {

    private AddExpenseFragmentBinding binding;
    private ArrayAdapter<Person> payerChoiceDropdownAdapter;
    private PersonInvolvedChoiceAdapter adapter;
    private AddExpenseViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_expense_fragment, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AddExpenseFragmentArgs args = AddExpenseFragmentArgs.fromBundle(requireArguments());
        long groupId = args.getGroupId();

        AddExpenseViewModelFactory factory = new AddExpenseViewModelFactory(
                requireActivity().getApplication(), groupId);

        viewModel = new ViewModelProvider(this, factory)
                .get(AddExpenseViewModel.class);


        setupToolbar();
        setupDescriptionInput();
        setupAmountInput();
        setupPayerInput(binding.payer);
        setupPeopleInvolvedSelection();
        subscribeUi();

        long expenseId = args.getExpenseId();
        if (expenseId != 0) {
            viewModel.getExpenseWithPeopleInvolved(expenseId).observe(getViewLifecycleOwner(), next -> {
                viewModel.setExpense(next);
                binding.setViewmodel(viewModel);
                adapter.notifyDataSetChanged();
                setupDateInput(binding.dateInput);
            });
        }
        else {
            binding.setViewmodel(viewModel);
            setupDateInput(binding.dateInput);
        }

    }

    private void setupToolbar() {
        MaterialToolbar toolbar = binding.toolbar;

        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.save_expense) {
                if (viewModel.areAllFieldsSet()) {
                    viewModel.save();
                    requireActivity().onBackPressed();
                    return true;
                }
            }

            return false;
        });
    }

    private void setupDateInput(TextInputEditText dateInput) {
        LocalDate selectedDate = LocalDate.parse(viewModel.date, viewModel.formatter);

        DatePickerDialog datePicker =
                new DatePickerDialog(requireContext(), (v, year, month, dayOfMonth) ->
                        dateInput.setText(
                                LocalDate.of(year, month + 1, dayOfMonth)
                                        .format(viewModel.formatter)), selectedDate.getYear(), selectedDate.getMonth().getValue() - 1, selectedDate.getDayOfMonth());

        dateInput.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(dateInput.getWindowToken(), 0);

            datePicker.show();
        });
    }

    private void setupAmountInput() {
        TextInputLayout textInputLayout = binding.expenseAmount;
        EditText editText = textInputLayout.getEditText();
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String content = editText.getText().toString();

                int dotIndex = content.indexOf('.');
                if (!content.isEmpty()) {
                    if (dotIndex == -1)
                        content = content + ".00";
                    else if (dotIndex == content.length() - 1)
                        content = content + "00";
                    else if (dotIndex == content.length() - 2)
                        content = content + "0";
                }

                editText.setText(content);

                if (content.isEmpty()) {
                    textInputLayout.setError("Pole jest wymagane");
                }
            }

            else {
                textInputLayout.setError(null);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                int dotIndex = text.indexOf('.');
                if (dotIndex != -1 && text.length() > dotIndex + 1 + 2) {
                    editText.setText(text.substring(0, dotIndex + 3));

                    if (editText.hasFocus())
                        editText.setSelection(text.length() - 1);
                }
            }
        });

    }

    private void setupDescriptionInput() {
        TextInputLayout textInputLayout = binding.expenseDescr;
        EditText editText = textInputLayout.getEditText();

        if (AddExpenseFragmentArgs.fromBundle(requireArguments()).getExpenseId() == 0 && editText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String content = editText.getText().toString().trim();
                editText.setText(content);

                if (content.isEmpty())
                    textInputLayout.setError("Pole jest wymagane");
            }

            else {
                textInputLayout.setError(null);
            }
        });
    }

    private void setupPayerInput(TextInputLayout textInputLayout) {
        MaterialAutoCompleteTextView editText = (MaterialAutoCompleteTextView) textInputLayout.getEditText();

        payerChoiceDropdownAdapter = new ArrayAdapter<>(
                requireContext(), R.layout.expense_payer_dropdown_item, new ArrayList<>());

        binding.newExpensePayerDropdown.setAdapter(payerChoiceDropdownAdapter);

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String content = editText.getText().toString().trim();
                editText.setText(content);

                if (content.isEmpty())
                    textInputLayout.setError("Pole jest wymagane");

                else if (viewModel.findPersonWithName(content) == null)
                    textInputLayout.setError("W grupie nie ma osoby o podanym imieniu");
            }

            else
                textInputLayout.setError(null);
        });
    }

    private void setupPeopleInvolvedSelection() {
        RecyclerView recyclerView = binding.peopleInvolvedChoice;
        adapter = new PersonInvolvedChoiceAdapter(viewModel.getSelectedItemIds());
        recyclerView.setAdapter(adapter);
    }

    private void subscribeUi() {
        viewModel.getAllPeople().observe(getViewLifecycleOwner(), people -> {
            if (people != null) {
               payerChoiceDropdownAdapter.clear();
               payerChoiceDropdownAdapter.addAll(people);
               adapter.submitList(people);

               // easiest way for items to stay selected when off-screen
               binding.peopleInvolvedChoice.setItemViewCacheSize(people.size());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = requireActivity().getCurrentFocus();
        if (currentFocus != null)
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }
}
