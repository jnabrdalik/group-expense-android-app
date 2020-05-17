package com.example.groupexpenseapp.db.converter;


        import androidx.room.TypeConverter;

        import org.threeten.bp.LocalDate;
        import org.threeten.bp.OffsetDateTime;
        import org.threeten.bp.format.DateTimeFormatter;

public class DateConverter {
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @TypeConverter
    public static OffsetDateTime toOffsetDateTime(String value) {
        return OffsetDateTime.from(dateTimeFormatter.parse(value));
    }

    @TypeConverter
    public static String fromOffsetDateTime(OffsetDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    @TypeConverter
    public static LocalDate toLocalDate(String value) {
        return LocalDate.from(dateFormatter.parse(value));
    }

    @TypeConverter
    public static String fromLocalDate(LocalDate date) {
        return date.format(dateFormatter);
    }
}
