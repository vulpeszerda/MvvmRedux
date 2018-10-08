package com.github.vulpeszerda.mvvmreduxsample.model;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = Todo.TABLE_NAME)
public class Todo {

    public static final String TABLE_NAME = "todo";
    public static final String FIELD_UID = "uid";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_COMPLETED = "completed";
    public static final String FIELD_CREATED_AT = "created_at";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = FIELD_UID)
    private long uid = 0;

    @NonNull
    @ColumnInfo(name = FIELD_TITLE)
    private String title = "";

    @NonNull
    @ColumnInfo(name = FIELD_MESSAGE)
    private String message = "";

    @ColumnInfo(name = FIELD_COMPLETED)
    private boolean completed = false;

    @NonNull
    @ColumnInfo(name = FIELD_CREATED_AT)
    private Date createdAt = new Date();

    @NonNull
    public static Todo create(@NonNull String title, @NonNull String message, boolean completed) {
        Todo todo = new Todo();
        todo.title = title;
        todo.message = message;
        todo.completed = completed;
        return todo;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @NonNull
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Todo(uid=" + uid + ", " +
                "title=" + title + ", " +
                "message=" + message + ", " +
                "completed=" + completed + ", " +
                "createdAt=" + createdAt + ")";
    }
}
