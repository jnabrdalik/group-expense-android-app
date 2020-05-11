package com.example.groupexpenseapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseAndPayer;

import java.util.List;

@Dao
public abstract class ExpenseDao {
    @Insert
    public abstract long insert(Expense expense);

    @Update
    public abstract void update(Expense expense);

    @Delete
    public abstract void delete(Expense expense);

    @Transaction
    @Query("select * from expenses where id = :expenseId")
    public abstract LiveData<ExpenseAndPayer> getExpenseAndPayer(long expenseId);

    @Transaction
    @Query("select * from expenses where group_id = :groupId order by datetime(time_added) desc")
    public abstract LiveData<List<ExpenseAndPayer>> getExpensesFromGroupNewestFirst(long groupId);

    @Transaction
    @Query("select * from expenses where group_id = :groupId order by amount desc")
    public abstract LiveData<List<ExpenseAndPayer>> getExpensesFromGroupMostExpensiveFirst(long groupId);

    @Transaction
    @Query("select * from expenses where group_id = :groupId order by amount asc")
    public abstract LiveData<List<ExpenseAndPayer>> getExpensesAndPayersFromGroupMostExpensiveFirst(long groupId);

    @Transaction
    public void updatePeopleInvolved(long expenseId, int[] peopleRemovedIds, int[] peopleAddedIds) {
        for (long personId : peopleAddedIds)
            insertPersonOwing(expenseId, personId);


        for (long personId : peopleRemovedIds)
            deletePersonOwing(expenseId, personId);
    }

    @Query("insert into expenses_people(expense_id, person_id) values(:expenseId, :personId)")
    public abstract void insertPersonOwing(long expenseId, long personId);

    @Query("delete from expenses_people where expense_id = :expenseId and person_id = :personId")
    abstract void deletePersonOwing(long expenseId, long personId);
}
