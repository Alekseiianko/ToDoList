package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Task> tasks;
    private MainActivity mainActivity;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Task task = tasks.get(position);

        holder.taskTextView.setText(task.getName());
        holder.dateTextView.setText("Run until " + task.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                mainActivity.addAndEditTasks(true, task, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView taskTextView;
        public TextView dateTextView;


        public MyViewHolder(View view) {
            super(view);
            taskTextView = view.findViewById(R.id.taskTextView);
            dateTextView = view.findViewById(R.id.dateTextView);

        }
    }

    public TaskAdapter(Context context, ArrayList<Task> tasks, MainActivity mainActivity) {
        this.context = context;
        this.tasks = tasks;
        this.mainActivity = mainActivity;
    }
}
