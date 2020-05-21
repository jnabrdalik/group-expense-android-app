package com.example.groupexpenseapp.ui.adapter.diffutil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.groupexpenseapp.debt.Debt;

public class DebtDiffUtil extends DiffUtil.ItemCallback<com.example.groupexpenseapp.debt.Debt> {
    @Override
    public boolean areItemsTheSame(@NonNull Debt oldItem, @NonNull Debt newItem) {
        return oldItem.getCreditor().getId() == newItem.getCreditor().getId() &&
                oldItem.getDebtor().getId() == newItem.getDebtor().getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Debt oldItem, @NonNull Debt newItem) {
        return oldItem.getCreditor().getName().equals(newItem.getCreditor().getName()) &&
                oldItem.getDebtor().getName().equals(newItem.getDebtor().getName()) &&
                oldItem.getAmount() == newItem.getAmount();
    }
}
