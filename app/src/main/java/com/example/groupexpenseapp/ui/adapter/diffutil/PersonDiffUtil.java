package com.example.groupexpenseapp.ui.adapter.diffutil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.groupexpenseapp.db.entity.Person;

public class PersonDiffUtil extends DiffUtil.ItemCallback<Person> {
    @Override
    public boolean areItemsTheSame(@NonNull Person oldItem, @NonNull Person newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Person oldItem, @NonNull Person newItem) {
        return oldItem.getId() == newItem.getId() &&
                oldItem.getName().equals(newItem.getName());
    }
}
