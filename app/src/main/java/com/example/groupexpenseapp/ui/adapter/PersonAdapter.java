package com.example.groupexpenseapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.PersonItemBinding;
import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.ui.adapter.diffutil.PersonDiffUtil;


public class PersonAdapter extends ListAdapter<Person, PersonAdapter.PersonViewHolder> {
    public PersonAdapter() {
        super(new PersonDiffUtil());

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PersonItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.person_item,
                        parent, false);
        return new PersonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = getItem(position);
        holder.binding.setPerson(person);
        holder.binding.executePendingBindings();
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {
        final PersonItemBinding binding;

        public PersonViewHolder(PersonItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}

