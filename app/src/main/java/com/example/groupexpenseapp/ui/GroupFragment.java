package com.example.groupexpenseapp.ui;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.GroupListFragmentBinding;

public class GroupFragment extends Fragment {
    private GroupListFragmentBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.group_fragment, container, false);


        return binding.getRoot();
    }
}
