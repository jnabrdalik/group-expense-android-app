package com.example.groupexpenseapp.ui.adapter.diffutil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.groupexpenseapp.db.entity.ExpenseWithPeopleInvolved;

public class ExpenseWithPeopleInvolvedDiffUtil extends DiffUtil.ItemCallback<ExpenseWithPeopleInvolved>{

    @Override
    public boolean areItemsTheSame(@NonNull ExpenseWithPeopleInvolved oldItem, @NonNull ExpenseWithPeopleInvolved newItem) {
        return oldItem.getExpense().getId() == newItem.getExpense().getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ExpenseWithPeopleInvolved oldItem, @NonNull ExpenseWithPeopleInvolved newItem) {
        return oldItem.getExpense().getId() == newItem.getExpense().getId() &&
                oldItem.getExpense().getDescription().equals(newItem.getExpense().getDescription()) &&
                oldItem.getExpense().getAmount() == newItem.getExpense().getAmount() &&
                oldItem.getPeopleInvolved().equals(newItem.getPeopleInvolved());
    }

}
