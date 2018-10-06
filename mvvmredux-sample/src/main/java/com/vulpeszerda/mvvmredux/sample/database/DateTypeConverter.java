package com.vulpeszerda.mvvmredux.sample.database;

import androidx.room.TypeConverter;

import java.util.Date;

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
