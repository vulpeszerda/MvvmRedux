package com.github.vulpeszerda.mvvmredux.sample.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.github.vulpeszerda.mvvmredux.sample.model.Todo;

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
