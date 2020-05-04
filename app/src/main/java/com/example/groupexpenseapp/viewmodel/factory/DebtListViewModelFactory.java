package com.example.groupexpenseapp.viewmodel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.groupexpenseapp.viewmodel.DebtListViewModel;


public class DebtListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final Application application;
    private final int groupId;

    public DebtListViewModelFactory(@NonNull Application application, int groupId) {
        this.application = application;
        this.groupId = groupId;
    }

    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        try {
//            return (T) Class.forName(modelClass.getName()).getConstructor(Application.class, Integer.TYPE).newInstance(application, groupId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return (T) new DebtListViewModel(application, groupId);
    }
}
