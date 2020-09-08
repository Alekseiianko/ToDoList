package com.example.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAppDatabase taskAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        taskAppDatabase = Room.databaseBuilder(getApplicationContext(),
                TaskAppDatabase.class, "TasksDB")
                .build();

        new GetAllTaskAsyncTask().execute();

        taskAdapter = new TaskAdapter(this, taskArrayList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(taskAdapter);


        FloatingActionButton floatingActionButton =
                findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditTasks(false, null, -1);
            }


        });


    }

    public void addAndEditTasks(final boolean isUpdate, final Task task, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.add_item, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        TextView newTaskTitle = view.findViewById(R.id.newTaskTitle);
        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText dateEditText = view.findViewById(R.id.dateEditText);

        newTaskTitle.setText(!isUpdate ? "Add Task" : "Edit Task");

        if (isUpdate && task != null) {
            nameEditText.setText(task.getName());
            dateEditText.setText(task.getDate());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton(isUpdate ? "Delete" : "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {

                                if (isUpdate) {

                                    deleteTask(task, position);
                                } else {

                                    dialogBox.cancel();

                                }

                            }
                        });


        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(nameEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter task name!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(dateEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter task date!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }


                if (isUpdate && task != null) {

                    updateTask(nameEditText.getText().toString(), dateEditText.getText().toString(), position);
                } else {

                    createTask(nameEditText.getText().toString(), dateEditText.getText().toString());
                }
            }
        });
    }

    private void deleteTask(Task task, int position) {

        taskArrayList.remove(position);

        new DeleteTaskAsyncTask().execute(task);

    }

    private void updateTask(String name, String date, int position) {

        Task task = taskArrayList.get(position);

        task.setName(name);
        task.setDate(date);

        new UpdateTaskAsyncTask().execute(task);

        taskArrayList.set(position, task);


    }

    private void createTask(String name, String date) {

        new CreateTaskAsyncTask().execute(new Task(0, name, date));

    }

    private class GetAllTaskAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            taskArrayList.addAll(taskAppDatabase.getTaskDAO().getAllTasks());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            taskAdapter.notifyDataSetChanged();
        }
    }


    private class CreateTaskAsyncTask extends AsyncTask<Task, Void, Void> {


        @Override
        protected Void doInBackground(Task... tasks) {

            long id = taskAppDatabase.getTaskDAO().addTask(
                    tasks[0]
            );


            Task task = taskAppDatabase.getTaskDAO().getTask(id);

            if (task != null) {

                taskArrayList.add(0, task);


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            taskAdapter.notifyDataSetChanged();
        }
    }

    private class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            taskAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Task... tasks) {

            taskAppDatabase.getTaskDAO().updateTask(tasks[0]);

            return null;
        }
    }

    private class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            taskAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Task... tasks) {

            taskAppDatabase.getTaskDAO().deleteTask(tasks[0]);


            return null;
        }
    }


}
