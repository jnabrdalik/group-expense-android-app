package com.example.groupexpenseapp.ui;

import com.example.groupexpenseapp.db.entity.Group;
import com.example.groupexpenseapp.db.entity.GroupWithSummary;

public interface GroupLongClickCallback {
    void onLongClick(GroupWithSummary group);
}
