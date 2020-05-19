package com.example.groupexpenseapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.ExpenseDetailsFragmentBinding;
import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.ui.adapter.PersonInvolvedAdapter;
import com.example.groupexpenseapp.viewmodel.ExpenseDetailsViewModel;
import com.example.groupexpenseapp.viewmodel.factory.ExpenseDetailsViewModelFactory;

public class ExpenseDetailsFragment extends Fragment {
    private ExpenseDetailsFragmentBinding binding;
    private PersonInvolvedAdapter adapter;
    private ExpenseDetailsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.expense_details_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        long expenseId = ExpenseDetailsFragmentArgs.fromBundle(requireArguments()).getExpenseId();
        ExpenseDetailsViewModelFactory factory = new ExpenseDetailsViewModelFactory(
                requireActivity().getApplication(), expenseId);

        viewModel = new ViewModelProvider(this, factory)
                .get(ExpenseDetailsViewModel.class);

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewmodel(viewModel);

        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
//        binding.toolbar.setOnMenuItemClickListener(item -> {
//            if (item.getItemId() == R.id.edit_expense) {
//                ExpenseDetailsFragmentDirections.actionExpenseDetailsFragmentToAddExpenseFragment(viewModel.).;
//                return true;
//            }
//
//            return false;
//        });

        adapter = new PersonInvolvedAdapter();
        subscribeUi();
        binding.peopleInvolved.setAdapter(adapter);
    }

    private void subscribeUi() {
        viewModel.getPeopleInvolved().observe(getViewLifecycleOwner(), next -> {
            if (next != null)
                adapter.submitList(next);
        });

        viewModel.getExpenseAndPayer().observe(getViewLifecycleOwner(), next -> {
            if (next != null) {
                binding.toolbar.setOnMenuItemClickListener(item -> {
                    Expense expense = next.getExpense();
                    if (item.getItemId() == R.id.edit_expense) {
                        NavDirections directions = ExpenseDetailsFragmentDirections
                                .actionExpenseDetailsFragmentToAddExpenseFragment(expense.getGroupId())
                                .setExpenseId(expense.getId());
                        Navigation.findNavController(binding.toolbar).navigate(directions);
                        return true;
                    }
                    else if (item.getItemId() == R.id.delete_expense) {
                        displayGroupDeleteWarningDialog(expense);
                        return true;
                    }

                    return false;
                });
            }
        });
    }

    private void displayGroupDeleteWarningDialog(Expense expense) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Usuń wydatek")
                .setMessage("Czy na pewno chcesz usunąć ten wydatek? Tej operacji nie można cofnąć.")
                .setPositiveButton("Usuń", (dialog, which) -> {
                    requireActivity().onBackPressed();
                    viewModel.deleteExpense(expense);
                })
                .setNegativeButton("Anuluj", null)
                .show();
    }
}
