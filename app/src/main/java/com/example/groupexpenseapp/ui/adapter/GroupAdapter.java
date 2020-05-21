package com.example.groupexpenseapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpenseapp.R;
import com.example.groupexpenseapp.databinding.GroupItemBinding;
import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.GroupWithSummary;
import com.example.groupexpenseapp.ui.fragment.GroupListFragmentDirections;
import com.example.groupexpenseapp.ui.GroupLongClickCallback;

import java.util.List;
// TODO switch to ListAdapter
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    GroupLongClickCallback longClickCallback;
    private List<GroupWithSummary> groups;

    public GroupAdapter(GroupLongClickCallback longClickCallback) {
        this.longClickCallback = longClickCallback;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return groups.get(position).getGroup().getId();
    }

    public void setGroupList(final List<GroupWithSummary> newGroups) {
        if (groups == null) {
            groups = newGroups;
            notifyItemRangeChanged(0, groups.size());
        }
        else {

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
                    return groups.get(oldItemPosition).getGroup().getId() == newGroups.get(newItemPosition).getGroup().getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    GroupWithSummary newGroup = newGroups.get(newItemPosition);
                    GroupWithSummary oldGroup = groups.get(oldItemPosition);

                    return newGroup.getGroup().getId() == oldGroup.getGroup().getId() &&
                            newGroup.getGroup().getName().equals(oldGroup.getGroup().getName()) &&
                            newGroup.getExpenseSum() == oldGroup.getExpenseSum() &&
                            newGroup.getPersonCount() == oldGroup.getPersonCount();
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
        GroupWithSummary group = groups.get(position);
        NavDirections directions = GroupListFragmentDirections.actionGroupListFragmentToGroupFragment(group.getGroup().getId());
        holder.binding.setClickListener(v -> Navigation.findNavController(v).navigate(directions));
        holder.binding.groupCardView.setOnLongClickListener(view -> {
            longClickCallback.onLongClick(group);
            return true;
        });
        holder.binding.setGroup(group.getGroup());
        holder.binding.setExpenseSum(group.getExpenseSum());
        holder.binding.setPersonCount(group.getPersonCount());
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (groups == null)
            return 0;

        return groups.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {

        final GroupItemBinding binding;

        public GroupViewHolder(GroupItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
