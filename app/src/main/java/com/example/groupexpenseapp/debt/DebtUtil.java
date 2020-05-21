package com.example.groupexpenseapp.debt;

import com.example.groupexpenseapp.db.entity.Expense;
import com.example.groupexpenseapp.db.entity.ExpenseWithPeopleInvolved;
import com.example.groupexpenseapp.db.entity.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DebtUtil {
    private List<ExpenseWithPeopleInvolved> expensesWithPeople;

    public DebtUtil(List<ExpenseWithPeopleInvolved> expenseWithPeople) {
        this.expensesWithPeople = expenseWithPeople;
    }

    public List<Debt> getDebts() {
        List<Debt> list = new ArrayList<>();

        for (ExpenseWithPeopleInvolved expenseWithPeople : expensesWithPeople) {
            Person payer = expenseWithPeople.getPayer();
            Expense expense = expenseWithPeople.getExpense();
            List<Person> peopleInvolved = expenseWithPeople.getPeopleInvolved();

            double amountPerPerson = expense.getAmount() / peopleInvolved.size();
            for (Person person : peopleInvolved) {
                if (person.getId() > payer.getId())
                    list.add(new Debt(person, payer, amountPerPerson));
                else if (person.getId() < payer.getId())
                    list.add(new Debt(payer, person, -amountPerPerson));
            }
        }

        if (list.isEmpty())
            return list;

        Collections.sort(list, (debt1, debt2) -> {
            if (debt1.getDebtor().getId() == debt2.getDebtor().getId())
                return Long.compare(debt1.getCreditor().getId(), debt2.getCreditor().getId());

            return Long.compare(debt1.getDebtor().getId(), debt2.getDebtor().getId());
        });

        List<Debt> newList = new ArrayList<>();
        Debt first = list.get(0);
        int i = 1;
        Person debtor = first.getDebtor();
        Person creditor = first.getCreditor();
        double amount = first.getAmount();
        while (i < list.size()) {
            Debt debt = list.get(i);
            Person newDebtor = debt.getDebtor();
            Person newCreditor = debt.getCreditor();

            if (newDebtor.getId() == debtor.getId() && newCreditor.getId() == creditor.getId())
                amount += debt.getAmount();

            else {
                if (amount > 0)
                    newList.add(new Debt(debtor, creditor, amount));
                else if (amount < 0)
                    newList.add(new Debt(creditor, debtor, -amount));

                debtor = newDebtor;
                creditor = newCreditor;
                amount = debt.getAmount();
            }

            i++;
        }

        return newList;
    }
}
