package com.example.groupexpenseapp.db;

import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {
    private static String[] groupNames = {"Wyjazd", "Wakacje", "Impreza", "Wyjście do baru"};
    private static String[] names = {"Jan", "Michał", "Mateusz", "Jakub", "Piotr", "Kamil", "Marcin", "Anna", "Julia", "Karolina"};
    private static String[] expenseDescriptions = {"Zakupy", "Jedzenie", "Piwo", "Bilety", "Napoje", "Paliwo", "Kino"};

    private DataGenerator() {

    }

    public static List<String> getRandomNames() {
        List<String> arrayOfNames = Arrays.asList(names);
        Collections.shuffle(arrayOfNames);
        // minimum 4 people in a group
        int maxIndex = ThreadLocalRandom.current().nextInt(4, names.length);

        return arrayOfNames.subList(0, maxIndex);
    }

    public static List<String> getRandomExpenseDescriptions() {
        List<String> arrayOfDescriptions = Arrays.asList(expenseDescriptions);
        Collections.shuffle(arrayOfDescriptions);
        //minimum 3 expenses
        int maxIndex = ThreadLocalRandom.current().nextInt(3, expenseDescriptions.length);

        return arrayOfDescriptions.subList(0, maxIndex);
    }

    public static List<String> getGroupNames() {
        List<String> arrayOfNames = Arrays.asList(groupNames);
        Collections.shuffle(arrayOfNames);

        return arrayOfNames;
    }

    public static double getRandomAmount() {
        return ThreadLocalRandom.current().nextDouble(0.5, 500.0);
    }

    public static OffsetDateTime getRandomDateTime() {
        long currentTimeEpochSecond = OffsetDateTime.now().toEpochSecond();
        long yearAgoEpochSecond = currentTimeEpochSecond - 365 * 24 * 60 * 60;
        long randomEpochSecond = ThreadLocalRandom.current().nextLong(yearAgoEpochSecond, currentTimeEpochSecond);

        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(randomEpochSecond), ZoneId.systemDefault());
    }

    public static int selectRandomId(List<Integer> ids) {
        int id = ThreadLocalRandom.current().nextInt(ids.size());

        return ids.get(id);
    }

    public static List<Integer> selectRandomIds(List<Integer> ids) {
        ArrayList<Integer> listCopy = new ArrayList<>(ids);
        Collections.shuffle(listCopy);
        //minimum 2 ids
        int maxIndex = ThreadLocalRandom.current().nextInt(2, listCopy.size());

        return listCopy.subList(0, maxIndex);
    }
}
