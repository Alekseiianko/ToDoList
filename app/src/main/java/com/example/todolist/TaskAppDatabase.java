package com.example.todolist;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskAppDatabase extends RoomDatabase {

    public abstract TaskDAO getTaskDAO();

}
