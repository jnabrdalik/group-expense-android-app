package com.example.groupexpenseapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.DebtItemBinding;
import com.example.groupexpenseapp.debt.Debt;
import com.example.groupexpenseapp.ui.adapter.diffutil.DebtDiffUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DebtAdapter extends ListAdapter<Debt, DebtAdapter.DebtViewHolder> {
    //TODO change to hashset
    private Set<Debt> selectedItems;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    public DebtAdapter(Set<Debt> selectedItems, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        super(new DebtDiffUtil());

        this.selectedItems = selectedItems;
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    @NonNull
    @Override
    public DebtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DebtItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.debt_item,
                        parent, false);

        return new DebtViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtViewHolder holder, int position) {
        Debt debt = getItem(position);
        holder.binding.setDebt(debt);

        CheckBox checkBox = holder.binding.checkBox;


        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                selectedItems.add(debt);
            else
                selectedItems.remove(debt);

            onCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
        });
        checkBox.setChecked(selectedItems.contains(debt));

        holder.binding.getRoot().setOnClickListener(v -> checkBox.toggle());

        holder.binding.executePendingBindings();
    }


    class DebtViewHolder extends RecyclerView.ViewHolder {
        final DebtItemBinding binding;

        public DebtViewHolder(@NonNull DebtItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
