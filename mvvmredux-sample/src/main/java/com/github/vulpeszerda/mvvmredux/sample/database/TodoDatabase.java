package com.github.vulpeszerda.mvvmredux.sample.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import androidx.annotation.NonNull;

import com.github.vulpeszerda.mvvmredux.sample.BuildConfig;
import com.github.vulpeszerda.mvvmredux.sample.model.Todo;

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
