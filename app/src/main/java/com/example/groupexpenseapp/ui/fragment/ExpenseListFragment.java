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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.ExpenseListFragmentBinding;
import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseWithPeopleInvolved;
import com.example.groupexpenseapp.ui.adapter.ExpenseAdapter;
import com.example.groupexpenseapp.viewmodel.ExpenseListViewModel;
import com.example.groupexpenseapp.viewmodel.factory.ExpenseListViewModelFactory;

import java.util.List;

public class ExpenseListFragment extends Fragment {
    private ExpenseListViewModel viewModel;
    private ExpenseListFragmentBinding binding;
    private ExpenseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.expense_list_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        long groupId = requireArguments().getLong("GROUP_ID");
        ExpenseListViewModelFactory factory = new ExpenseListViewModelFactory(
                requireActivity().getApplication(), groupId);

        viewModel = new ViewModelProvider(this, factory)
                .get(ExpenseListViewModel.class);

        ExpenseAdapter.OnHolderButtonsClickedListener listener = new ExpenseAdapter.OnHolderButtonsClickedListener() {
            @Override
            public void onEdit(Expense expense) {
                NavDirections directions = GroupDetailsFragmentDirections
                        .actionGroupFragmentToAddExpenseFragment(expense.getGroupId())
                        .setExpenseId(expense.getId());
                Navigation.findNavController(requireView()).navigate(directions);
            }

            @Override
            public void onDelete(Expense expense) {
                displayGroupDeleteWarningDialog(expense);
            }
        };
        adapter = new ExpenseAdapter(listener);
        binding.expenses.setAdapter(adapter);
        binding.expenses.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        NavDirections directions = GroupDetailsFragmentDirections.actionGroupFragmentToAddExpenseFragment(groupId);
        binding.addGroupButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(directions));
        subscribeUi(viewModel.getGroupExpenses());

    }

    private void subscribeUi(LiveData<List<ExpenseWithPeopleInvolved>> groupExpenses) {
        groupExpenses.observe(getViewLifecycleOwner(), expenses -> {
            if (expenses != null)
                adapter.submitList(expenses);
        });
    }

    private void displayGroupDeleteWarningDialog(Expense expense) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Usuń wydatek")
                .setMessage("Czy na pewno chcesz usunąć ten wydatek? Tej operacji nie można cofnąć.")
                .setPositiveButton("Usuń", (dialog, which) -> viewModel.deleteExpense(expense))
                .setNegativeButton("Anuluj", null)
                .show();
    }
}
