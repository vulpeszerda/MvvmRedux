package com.vulpeszerda.mvvmredux.sample.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.vulpeszerda.mvvmredux.sample.BuildConfig;
import com.vulpeszerda.mvvmredux.sample.model.Todo;

@Database(entities = {Todo.class},
          version = BuildConfig.DATABASE_VERSION)
@TypeConverters(value = {DateTypeConverter.class})
public abstract class TodoDatabase extends RoomDatabase {

    private static volatile TodoDatabase instance;

    @NonNull
    public static TodoDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (TodoDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TodoDatabase.class, BuildConfig.DATABASE_NAME)
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract TodoDao todoDao();


}
