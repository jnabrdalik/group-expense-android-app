package com.example.groupexpenseapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.groupexpenseapp.db.entity.Person;
import com.example.groupexpenseapp.db.entity.PersonWithBalance;

import java.util.List;

@Dao
public abstract class PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(Person person);

    @Delete
    abstract void delete(Person person);

    @Transaction
    public boolean deleteWithCheck(Person person) {
        long id = person.getId();
        if (paidForCount(id) == 0 && owesCount(id) == 0) {
            delete(person);
            return true;
        }

        return false;
    }

    @Query("select count(*) from persons p join expenses e on p.id = e.payer_id where p.id = :personId")
    abstract int paidForCount(long personId);

    @Query("select count(*) from persons p join expenses_people ep on p.id = ep.person_id where p.id = :personId")
    abstract int owesCount(long personId);

    @Query("select * from persons where group_id = :groupId order by name")
    public abstract LiveData<List<Person>> getPeopleFromGroup(long groupId);

    @Transaction
    @Query("select * from persons where id = :personId")
    public abstract LiveData<PersonWithBalance> getPersonWithRelatedExpenses(long personId);

    @Query("select * from persons join expenses_people on persons.id = expenses_people.person_id where expenses_people.expense_id = :expenseId")
    public abstract LiveData<List<Person>> getPeopleOwingForExpense(long expenseId);

    //only temp
    @Query("delete from persons")
    public abstract void deleteAll();

}
