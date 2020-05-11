package com.example.groupexpenseapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.ExpenseItemBinding;
import com.example.groupexpenseapp.db.entity.ExpenseAndPayer;
import com.example.groupexpenseapp.ui.adapter.diffutil.ExpenseAndPayerDiffUtil;
import com.example.groupexpenseapp.ui.fragment.GroupDetailsFragmentDirections;

public class ExpenseAdapter extends ListAdapter<ExpenseAndPayer, ExpenseAdapter.ExpenseViewHolder> {

    public ExpenseAdapter() {
        super(new ExpenseAndPayerDiffUtil());

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
        ExpenseAndPayer expenseAndPayer = getItem(position);
        NavDirections directions = GroupDetailsFragmentDirections.actionGroupFragmentToExpenseDetailsFragment(expenseAndPayer.getExpense().getId());
        holder.binding.setClickListener(v -> Navigation.findNavController(v).navigate(directions));
        holder.binding.setExpenseAndPayer(expenseAndPayer);
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
