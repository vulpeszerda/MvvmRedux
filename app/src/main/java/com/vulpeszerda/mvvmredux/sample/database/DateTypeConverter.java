package com.vulpeszerda.mvvmredux.sample.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by vulpes on 2017. 7. 19..
 */

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
