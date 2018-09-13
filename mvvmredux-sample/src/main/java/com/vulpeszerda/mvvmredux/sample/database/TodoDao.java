package com.vulpeszerda.mvvmredux.sample.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.vulpeszerda.mvvmredux.sample.model.Todo;

import java.util.List;

@Dao
public interface TodoDao {

    @Nullable
    @WorkerThread
    @Query("SELECT * FROM " + Todo.TABLE_NAME + " WHERE " + Todo.FIELD_UID + " = :uid LIMIT 1")
    Todo findByUid(long uid);

    @NonNull
    @WorkerThread
    @Query("SELECT * FROM " + Todo.TABLE_NAME)
    List<Todo> all();

    @WorkerThread
    @Query("DELETE FROM " + Todo.TABLE_NAME)
    void deleteAll();

    @WorkerThread
    @Query("DELETE FROM " + Todo.TABLE_NAME + " WHERE " + Todo.FIELD_UID + " IN (:uids)")
    void deleteByUids(long... uids);

    @WorkerThread
    @Delete
    void delete(@NonNull Todo todos);

    @NonNull
    @WorkerThread
    @Insert
    List<Long> insert(Todo... todos);

    @WorkerThread
    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Todo... todos);
}
