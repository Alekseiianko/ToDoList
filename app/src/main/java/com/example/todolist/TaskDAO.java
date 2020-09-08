package com.example.todolist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    public long addTask(Task task);

    @Update
    public void updateTask(Task task);

    @Delete
    public void deleteTask(Task task);

    @Query("select * from tasks")
    public List<Task> getAllTasks();

    @Query("select * from tasks where task_id ==:taskId ")
    public  Task getTask(long taskId);

}
