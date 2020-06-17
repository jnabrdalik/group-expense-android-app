package com.example.groupexpenseapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseWithPeopleInvolved;

import java.util.List;
import java.util.Set;

@Dao
public abstract class ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(Expense expense);

    @Delete
    public abstract void delete(Expense expense);

    @Transaction
    @Query("select * from expenses where id = :expenseId")
    public abstract LiveData<ExpenseWithPeopleInvolved> getExpenseWithPeopleInvolved(long expenseId);

    @Transaction
    public long updateOrInsertExpense(Expense expense, Set<Integer> peopleAddedIds, Set<Integer> peopleRemovedIds) {
        int expenseId = (int) insert(expense);

        for (int personId : peopleRemovedIds)
            deletePersonOwing(expenseId, personId);

        for (int personId : peopleAddedIds)
            insertPersonOwing(expenseId, personId);

        return expenseId;
    }


    @Query("insert into expenses_people(expense_id, person_id) values(:expenseId, :personId)")
    public abstract void insertPersonOwing(long expenseId, long personId);

    @Query("delete from expenses_people where expense_id = :expenseId and person_id = :personId")
    abstract void deletePersonOwing(long expenseId, long personId);

    @Transaction
    @Query("select * from expenses where group_id = :groupId")
    public abstract List<ExpenseWithPeopleInvolved> getExpensesAndPeopleInvolvedSync(long groupId);

    @Transaction
    @Query("select * from expenses where group_id = :groupId")
    public abstract LiveData<List<ExpenseWithPeopleInvolved>> getExpensesAndPeopleInvolved(long groupId);

    @Transaction
    @Query("select * from expenses where group_id = :groupId order by amount asc")
    public abstract LiveData<List<ExpenseWithPeopleInvolved>> getExpensesWithPeopleInvolvedFromGroupMostExpensiveFirst(long groupId);

    @Query("delete from expenses where id = :expenseId")
    public abstract void deleteById(long expenseId);
}
