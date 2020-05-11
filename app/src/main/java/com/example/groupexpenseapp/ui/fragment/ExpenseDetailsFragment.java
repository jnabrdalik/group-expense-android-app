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

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.ExpenseDetailsFragmentBinding;
import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.ui.adapter.PersonInvolvedAdapter;
import com.example.groupexpenseapp.viewmodel.ExpenseDetailsViewModel;
import com.example.groupexpenseapp.viewmodel.factory.ExpenseDetailsViewModelFactory;

import java.util.List;

public class ExpenseDetailsFragment extends Fragment {
    private ExpenseDetailsFragmentBinding binding;
    private PersonInvolvedAdapter adapter;

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

        final ExpenseDetailsViewModel viewModel = new ViewModelProvider(this, factory)
                .get(ExpenseDetailsViewModel.class);

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewmodel(viewModel);

        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        adapter = new PersonInvolvedAdapter();
        subscribeUi(viewModel.getPeopleInvolved());
        binding.peopleInvolved.setAdapter(adapter);
    }

    private void subscribeUi(LiveData<List<Person>> peopleInvolved) {
        peopleInvolved.observe(getViewLifecycleOwner(), next -> {
            if (next != null)
                adapter.submitList(next);
        });
    }
}
