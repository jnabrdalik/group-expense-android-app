package com.example.groupexpenseapp.ui.fragment;

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

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.DebtListFragmentBinding;
import com.example.groupexpenseapp.debt.Debt;
import com.example.groupexpenseapp.ui.adapter.DebtAdapter;
import com.example.groupexpenseapp.viewmodel.DebtListViewModel;
import com.example.groupexpenseapp.viewmodel.factory.DebtListViewModelFactory;

import java.util.List;


public class DebtListFragment extends Fragment {
    private DebtListFragmentBinding binding;
    private DebtListViewModel viewModel;
    private DebtAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        List<Debt> selectedItems = viewModel.getSelectedItems();
        adapter = new DebtAdapter(selectedItems);
        binding.debts.setAdapter(adapter);
        binding.debts.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));


        binding.button.setOnClickListener(v -> viewModel.payDebts());

        subscribeUi(viewModel.getDebts());
    }

    private void subscribeUi(LiveData<List<Debt>> debts) {
        debts.observe(getViewLifecycleOwner(), next -> {
            viewModel.resetSelection();
            if (next != null) {
                adapter.submitList(next);
                binding.debts.setItemViewCacheSize(next.size());
            }

        });
    }
}
