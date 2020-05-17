package com.example.groupexpenseapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.PersonInvolvedChoiceItemBinding;
import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.ui.adapter.diffutil.PersonDiffUtil;

import java.util.HashSet;

public class PersonInvolvedChoiceAdapter extends ListAdapter<Person, PersonInvolvedChoiceAdapter.PersonInvolvedChoiceViewHolder> {
    private HashSet<Integer> selectedItemsIds;

    public PersonInvolvedChoiceAdapter(HashSet<Integer> selectedItemsIds) {
        super(new PersonDiffUtil());

        this.selectedItemsIds = selectedItemsIds;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }


    @NonNull
    @Override
    public PersonInvolvedChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PersonInvolvedChoiceItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.person_involved_choice_item,
                        parent, false);

        return new PersonInvolvedChoiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonInvolvedChoiceViewHolder holder, int position) {
        Person person = getItem(position);
        holder.binding.setPerson(person);
        int personId = person.getId();

        holder.binding.checkBox.setChecked(selectedItemsIds.contains(personId));
        holder.binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                selectedItemsIds.add(person.getId());
            else
                selectedItemsIds.remove(person.getId());
        });

        holder.binding.executePendingBindings();
    }

    static class PersonInvolvedChoiceViewHolder extends RecyclerView.ViewHolder {
        private PersonInvolvedChoiceItemBinding binding;

        public PersonInvolvedChoiceViewHolder(PersonInvolvedChoiceItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
