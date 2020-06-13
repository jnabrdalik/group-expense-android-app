package com.example.groupexpenseapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.ExpenseItemBinding;
import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseWithPeopleInvolved;
import com.example.groupexpenseapp.ui.adapter.diffutil.ExpenseWithPeopleInvolvedDiffUtil;

public class ExpenseAdapter extends ListAdapter<ExpenseWithPeopleInvolved, ExpenseAdapter.ExpenseViewHolder> {
    private OnHolderButtonsClickedListener onHolderButtonsClickedListener;

    public ExpenseAdapter(OnHolderButtonsClickedListener onHolderButtonsClickedListener) {
        super(new ExpenseWithPeopleInvolvedDiffUtil());

        this.onHolderButtonsClickedListener = onHolderButtonsClickedListener;
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
        ExpenseWithPeopleInvolved expenseWithPeopleInvolved = getItem(position);

        holder.binding.setExpenseWithPeopleInvolved(expenseWithPeopleInvolved);
        holder.binding.deleteExpense.setOnClickListener(v -> onHolderButtonsClickedListener.onDelete(expenseWithPeopleInvolved.getExpense()));
        holder.binding.editExpense.setOnClickListener(v -> onHolderButtonsClickedListener.onEdit(expenseWithPeopleInvolved.getExpense()));
        holder.binding.executePendingBindings();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        final ExpenseItemBinding binding;

        public ExpenseViewHolder(ExpenseItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    public interface OnHolderButtonsClickedListener {
        void onEdit(Expense expense);
        void onDelete(Expense expense);
    }
}
