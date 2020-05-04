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
import com.example.groupexpenseapp.databinding.PersonListFragmentBinding;
import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.ui.adapter.PersonAdapter;
import com.example.groupexpenseapp.viewmodel.PersonListViewModel;
import com.example.groupexpenseapp.viewmodel.factory.PersonListViewModelFactory;

import java.util.List;

public class PersonListFragment extends Fragment {
    private PersonListFragmentBinding binding;
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

        final PersonListViewModel viewModel = new ViewModelProvider(this, factory)
                .get(PersonListViewModel.class);

        adapter = new PersonAdapter();
        binding.persons.setAdapter(adapter);
        binding.persons.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));
        subscribeUi(viewModel.getPersons());

    }

    private void subscribeUi(LiveData<List<Person>> allPersons) {
        allPersons.observe(getViewLifecycleOwner(), persons -> {
            if (persons != null)
                adapter.submitList(persons);
        });
    }
}
