package com.github.vulpeszerda.mvvmreduxsample.database;

import java.util.Date;

import androidx.room.TypeConverter;

public class DateTypeConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date.getTime();
    }
}
