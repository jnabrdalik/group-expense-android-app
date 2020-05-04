package com.example.groupexpenseapp.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groupexpenseapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebtListFragment extends Fragment {

    public DebtListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.debt_list_fragment, container, false);
    }
}
