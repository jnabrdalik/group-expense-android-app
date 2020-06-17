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

public class DebtAdapter extends ListAdapter<Debt, DebtAdapter.DebtViewHolder> {
    private OnDebtClickListener onDebtClickListener;

    public DebtAdapter(OnDebtClickListener onDebtClickListener) {
        super(new DebtDiffUtil());

        this.onDebtClickListener = onDebtClickListener;
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
        holder.binding.checkButton.setOnClickListener(v -> onDebtClickListener.onCheck(debt));
        holder.binding.getRoot().setOnLongClickListener(v -> {
            onDebtClickListener.onShare(debt);
            return true;
        });

        holder.binding.executePendingBindings();
    }


    static class DebtViewHolder extends RecyclerView.ViewHolder {
        final DebtItemBinding binding;

        public DebtViewHolder(@NonNull DebtItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    public interface OnDebtClickListener {
        void onCheck(Debt debt);
        void onShare(Debt debt);
    }
}
