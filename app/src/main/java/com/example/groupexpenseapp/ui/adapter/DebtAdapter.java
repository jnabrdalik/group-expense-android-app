package com.example.groupexpenseapp.ui.adapter;

import android.content.Intent;
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
    private OnDebtClickListener onCheckClickListener;
    private OnDebtClickListener onShareClickListener;

    public DebtAdapter(OnDebtClickListener onCheckClickListener, OnDebtClickListener onShareClickListener) {
        super(new DebtDiffUtil());

        this.onCheckClickListener = onCheckClickListener;
        this.onShareClickListener = onShareClickListener;
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
        holder.binding.checkButton.setOnClickListener(v -> onCheckClickListener.onClick(debt));
        holder.binding.shareButton.setOnClickListener(v -> onShareClickListener.onClick(debt));

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
        void onClick(Debt debt);
    }
}
