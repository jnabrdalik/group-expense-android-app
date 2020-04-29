package com.example.groupexpenseapp.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.GroupItemBinding;
import com.example.groupexpenseapp.db.entity.Group;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<Group> groups;

    public GroupAdapter() {
        setHasStableIds(true);
    }

    public void setGroups(final List<Group> newGroups) {
        if (groups == null) {
            groups = newGroups;
            //Log.d("ROZMIAR", String.valueOf(groups.size()));
            notifyItemRangeInserted(0, newGroups.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return groups.size();
                }

                @Override
                public int getNewListSize() {
                    return newGroups.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return groups.get(oldItemPosition).getId() ==
                            newGroups.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Group newGroup = newGroups.get(newItemPosition);
                    Group oldGroup = groups.get(oldItemPosition);
                    return newGroup.getId() == oldGroup.getId()
                            && TextUtils.equals(newGroup.getDescription(), oldGroup.getDescription())
                            && TextUtils.equals(newGroup.getName(), oldGroup.getName());
                }
            });
            groups = newGroups;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    @NonNull
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.group_item,
                        parent, false);

        return new GroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.binding.setGroup(groups.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return groups == null ? 0 : groups.size();
    }

    @Override
    public long getItemId(int position) {
        return groups.get(position).getId();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {

        final GroupItemBinding binding;

        public GroupViewHolder(GroupItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
