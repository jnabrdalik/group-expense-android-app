package com.example.groupexpenseapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.GroupDetailsFragmentBinding;
import com.example.groupexpenseapp.viewmodel.GroupViewModel;
import com.example.groupexpenseapp.viewmodel.factory.GroupViewModelFactory;
import com.google.android.material.tabs.TabLayoutMediator;

public class GroupDetailsFragment extends Fragment {

    private static final int N_OF_TABS = 3;

    private long groupId;
    private GroupDetailsFragmentBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.group_details_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        groupId = GroupDetailsFragmentArgs.fromBundle(requireArguments()).getGroupId();
        GroupViewModelFactory factory = new GroupViewModelFactory(
                requireActivity().getApplication(), groupId);

        final GroupViewModel viewModel = new ViewModelProvider(this, factory)
                .get(GroupViewModel.class);

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewmodel(viewModel);
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        binding.groupsRecyclerView.setAdapter(new GroupPagerAdapter(this));
        //TODO move to xml
        new TabLayoutMediator(binding.groupTabs, binding.groupsRecyclerView,
                ((tab, position) -> {
                    String name;
                    int iconId;

                    if (position == 0) {
                        name = "Wydatki";
                        iconId = R.drawable.ic_expense;
                    }
                    else if (position == 1) {
                        name = "Osoby";
                        iconId = R.drawable.ic_people;
                    }
                    else if (position == 2) {
                            name = "Długi";
                            iconId = R.drawable.ic_debts;
                        }
                    else
                        throw new IllegalArgumentException("Selected tab does not exist!");
                    tab.setText(name);
                    tab.setIcon(iconId);

                })).attach();

    }

    public class GroupPagerAdapter extends FragmentStateAdapter {

        public GroupPagerAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Bundle args = new Bundle();
            args.putLong("GROUP_ID", groupId);

            Fragment fragment;

            switch (position) {
                case 0:
                    fragment = new ExpenseListFragment();
                    break;

                case 1:
                    fragment = new PersonListFragment();
                    break;

                case 2:
                    fragment = new DebtListFragment();
                    break;

                default:
                    throw new IndexOutOfBoundsException();
            }

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return N_OF_TABS;
        }
    }
}
