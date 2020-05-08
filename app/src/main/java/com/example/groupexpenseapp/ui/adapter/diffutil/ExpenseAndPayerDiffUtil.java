package com.example.groupexpenseapp.ui.adapter.diffutil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.groupexpenseapp.db.entity.ExpenseAndPayer;

public class ExpenseAndPayerDiffUtil extends DiffUtil.ItemCallback<ExpenseAndPayer>{

    @Override
    public boolean areItemsTheSame(@NonNull ExpenseAndPayer oldItem, @NonNull ExpenseAndPayer newItem) {
        return oldItem.getExpense().getId() == newItem.getExpense().getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ExpenseAndPayer oldItem, @NonNull ExpenseAndPayer newItem) {
        return oldItem.getExpense().getId() == newItem.getExpense().getId() &&
                oldItem.getExpense().getDescription().equals(newItem.getExpense().getDescription()) &&
                oldItem.getExpense().getAmount() == newItem.getExpense().getAmount();
    }

}
