package com.example.rauan.todo.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rauan.todo.R;
import com.example.rauan.todo.database.Database;
import com.example.rauan.todo.model.ToDo;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<ToDo> toDoList;
    private GetAllToDoAsync getAllToDoAsync;
    private ToDoAdapter toDoAdapter;
    private ListView listViewMainActivity;
    private RemoveToDoFromDataBase removeToDoFromDataBase;
    FloatingActionButton floatingActionButton;

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*calendarView = (CalendarView) findViewById(R.id.calendarView);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editTextDescriptionToDo);*/
        /*calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
           @Override
           public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
               int d = dayOfMonth:month:year;
               textView.setText(d+"");
           }
       });*/
        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        String strTime = simpleDateFormat.format(new Date());
        textView.setText(strTime);*/

        //num = Integer.parseInt(editText.getText().toString());
        listViewMainActivity = (ListView) findViewById(R.id.listViewMainActivity);
        listViewMainActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDo toDo = toDoList.get(position);
                Intent intent = new Intent(MainActivity.this,ToDoActivity.class);
                intent.putExtra("todo_entity",toDo);
                startActivity(intent);

            }
        });
        listViewMainActivity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ToDo toDoRemove = toDoList.get(position);
                showRemoveContactDialog(toDoRemove);
                return true;
            }
        });
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ToDoActivity.class);
                startActivity(intent);

            }
        });


    }
    private void showRemoveContactDialog(final ToDo toDo){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("Do you want to remove ?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeToDoFromDataBase = new RemoveToDoFromDataBase(toDo);
                        removeToDoFromDataBase.execute();
                    }
                })
                .create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllToDoAsync = new GetAllToDoAsync();
        getAllToDoAsync.execute();
    }




    //Достаем из БД все TODO записи
    public class GetAllToDoAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Database database = new Database(MainActivity.this);
            toDoList = database.getAllToDo();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            toDoAdapter = new ToDoAdapter(MainActivity.this,toDoList);
            listViewMainActivity.setAdapter(toDoAdapter);
            super.onPostExecute(aVoid);
        }
    }
    public class RemoveToDoFromDataBase extends AsyncTask<Void,Void,Void>{
        private ToDo toDo;

        public RemoveToDoFromDataBase(ToDo toDo) {
            this.toDo = toDo;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Database database = new Database(MainActivity.this);
            database.removeToDo(toDo);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            toDoList.remove(toDo);
            toDoAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,"TODO was removed",Toast.LENGTH_SHORT).show();
        }
    }
}
//Comment in MainActivity for Test
