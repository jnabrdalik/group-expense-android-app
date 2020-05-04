package com.example.groupexpenseapp.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.CreateGroupFragmentBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGroupFragment extends DialogFragment {
    private CreateGroupFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.create_group_fragment, container, false);

        return binding.getRoot();
    }
}
