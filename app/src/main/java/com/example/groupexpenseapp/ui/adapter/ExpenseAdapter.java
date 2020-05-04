package com.example.groupexpenseapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.ExpenseItemBinding;
import com.example.groupexpenseapp.db.entity.ExpenseAndPayer;

public class ExpenseAdapter extends ListAdapter<ExpenseAndPayer, ExpenseAdapter.ExpenseViewHolder> {

    public ExpenseAdapter() {
        super(new DiffUtil.ItemCallback<ExpenseAndPayer>() {
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
        });

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getExpense().getId();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExpenseItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.expense_item,
                        parent, false);

        return new ExpenseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseAndPayer expense = getItem(position);
        holder.binding.setExpenseAndPayer(expense);
        holder.binding.executePendingBindings();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        final ExpenseItemBinding binding;

        public ExpenseViewHolder(ExpenseItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
