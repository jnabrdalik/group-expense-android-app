package com.example.groupexpenseapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.DebtItemBinding;
import com.example.groupexpenseapp.debt.Debt;
import com.example.groupexpenseapp.ui.adapter.diffutil.DebtDiffUtil;

import java.util.List;

public class DebtAdapter extends ListAdapter<Debt, DebtAdapter.DebtViewHolder> {
    //TODO change to hashset
    private List<Debt> selectedItems;

    public DebtAdapter(List<Debt> selectedItems) {
        super(new DebtDiffUtil());

        this.selectedItems = selectedItems;
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

        holder.binding.checkBox.setChecked(selectedItems.contains(debt));
        holder.binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                selectedItems.add(debt);
            else
                selectedItems.remove(debt);
        });

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
