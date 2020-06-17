package com.example.groupexpenseapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.DebtListFragmentBinding;
import com.example.groupexpenseapp.debt.Debt;
import com.example.groupexpenseapp.ui.adapter.DebtAdapter;
import com.example.groupexpenseapp.viewmodel.DebtListViewModel;
import com.example.groupexpenseapp.viewmodel.factory.DebtListViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DebtListFragment extends Fragment {
    private DebtListFragmentBinding binding;
    private DebtListViewModel viewModel;
    private DebtAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.debt_list_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        long groupId = requireArguments().getLong("GROUP_ID");
        DebtListViewModelFactory factory = new DebtListViewModelFactory(
                requireActivity().getApplication(), groupId);

        viewModel = new ViewModelProvider(this, factory)
                .get(DebtListViewModel.class);

        adapter = new DebtAdapter(new DebtAdapter.OnDebtClickListener() {
            @Override
            public void onCheck(Debt debt) {
                viewModel.removeDebt(debt).subscribe(expenseId -> {
                    Snackbar.make(requireView(), "Oznaczono dług jako rozliczony", Snackbar.LENGTH_SHORT)
                            .setAction("Cofnij", v -> viewModel.deleteExpense(expenseId))
                            .show();
                }).dispose();
            }

            @Override
            public void onShare(Debt debt) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, debt.getDebtor() + " -> " + debt.getCreditor());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Poinformuj znajomego o długu");
                startActivity(shareIntent);
            }
        });

        binding.debts.setAdapter(adapter);
        binding.debts.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        subscribeUi(viewModel.getDebts());
    }

    private void subscribeUi(LiveData<List<Debt>> debts) {
        debts.observe(getViewLifecycleOwner(), next -> {
            if (next != null) {
                adapter.submitList(next);
                binding.debts.setItemViewCacheSize(next.size());
            }
        });
    }
}
