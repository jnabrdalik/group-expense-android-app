package com.example.groupexpenseapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.PersonInvolvedItemBinding;
import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.ui.adapter.diffutil.PersonDiffUtil;

public class PersonInvolvedAdapter extends ListAdapter<Person, PersonInvolvedAdapter.PersonInvolvedViewHolder> {
    public PersonInvolvedAdapter() {
        super(new PersonDiffUtil());

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }


    @NonNull
    @Override
    public PersonInvolvedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PersonInvolvedItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.person_involved_item,
                        parent, false);

        return new PersonInvolvedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonInvolvedViewHolder holder, int position) {
        Person person = getItem(position);
        holder.binding.setPerson(person);
        holder.binding.executePendingBindings();
    }

    static class PersonInvolvedViewHolder extends RecyclerView.ViewHolder {
        final PersonInvolvedItemBinding binding;

        public PersonInvolvedViewHolder(PersonInvolvedItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
