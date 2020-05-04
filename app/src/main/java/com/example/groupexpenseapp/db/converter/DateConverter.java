package com.example.groupexpenseapp.db.converter;


        import androidx.room.TypeConverter;

        import org.threeten.bp.OffsetDateTime;
        import org.threeten.bp.format.DateTimeFormatter;

public class DateConverter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @TypeConverter
    public static OffsetDateTime toOffsetDateTime(String value) {
        return OffsetDateTime.from(formatter.parse(value));
    }

    @TypeConverter
    public static String fromOffsetDateTime(OffsetDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
