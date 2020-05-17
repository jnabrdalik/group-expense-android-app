package com.example.groupexpenseapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.groupexpenseapp.db.entity.ExpenseAndPayer;
import com.example.groupexpenseapp.ui.adapter.ExpenseAdapter;
import com.example.groupexpenseapp.viewmodel.ExpenseListViewModel;
import com.example.groupexpenseapp.viewmodel.factory.ExpenseListViewModelFactory;

import java.util.List;

public class ExpenseListFragment extends Fragment {
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

        final ExpenseListViewModel viewModel = new ViewModelProvider(this, factory)
                .get(ExpenseListViewModel.class);

        adapter = new ExpenseAdapter();
        binding.expenses.setAdapter(adapter);
        binding.expenses.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        NavDirections directions = GroupDetailsFragmentDirections.actionGroupFragmentToAddExpenseFragment(groupId);
        binding.addGroupButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(directions));
        subscribeUi(viewModel.getGroupExpenses());

    }

    private void subscribeUi(LiveData<List<ExpenseAndPayer>> groupExpenses) {
        groupExpenses.observe(getViewLifecycleOwner(), expenses -> {
            if (expenses != null)
                adapter.submitList(expenses);
        });
    }
}
