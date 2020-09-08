package com.example.todolist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private long id;

    @ColumnInfo(name = "task_name")
    private String name;

    @ColumnInfo(name = "task_date")
    private String date;

    @Ignore
    public Task() {
    }

    public Task(long id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Ignore
    public Task(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
