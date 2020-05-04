package com.example.groupexpenseapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseAndPayer;
import com.example.groupexpenseapp.db.entity.ExpenseWithPeopleInvolved;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertExpense(Expense expense);

    @Query("select * from expenses where group_id = :groupId order by datetime(time_added) desc")
    LiveData<List<Expense>> getExpensesFromGroupNewestFirst(long groupId);

    @Transaction
    @Query("select * from expenses where group_id = :groupId")
    LiveData<List<ExpenseAndPayer>> getExpensesAndPayers(long groupId);

    @Transaction
    @Query("select * from expenses where id = :expenseId")
    LiveData<ExpenseWithPeopleInvolved> getExpenseAndPeopleInvolved(long expenseId);

}
