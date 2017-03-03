package com.example.rauan.todo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rauan.todo.model.ToDo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rauan on 019 19.02.2017.
 */

public class Database{

    //Название базы данных
    public static final String DATABASE_NAME = "TODO_DB";

    //Название таблицы
    public static final String TABLE_NAME = "TODO";

    //Версия базы данных
    public static final int DATABASE_VERSION = 1;

    //Столбец для ID
    public static final String ID = "ID";

    public static final String TITLE_TODO = "TITLE";

    public static final String CATEGOTY_SPINNER_TODO = "CATEGORY_SPINNER";

    public static final String DESCRIPTION_TODO = "DESCRIPTION_TODO";

    public static final String START_DATE_TODO = "START_DATE_TODO";

    public static final String END_DATE_TODO = "END_DATE_TODO";

    //Класс для работы с базой данных
    private SQLiteDatabase database;
    //Класс для создания базы данных
    private DatabaseHelper helper;

    public Database(Context context) {
        helper = new DatabaseHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //Открываем базу данных
    public void open() {
        database = helper.getWritableDatabase();
    }


    //Закрываем базу данных
    public void close() {
        if (database != null) {
            database.close();
        }
    }

    //Создание таблицы (SQL команда для создания таблицы)
    // SQL statement to create book table
    public static final String CREATE_TABLE =
            "CREATE TABLE if not exists " + TABLE_NAME +
                    " (" + ID + " integer primary key autoincrement, " +
                    TITLE_TODO + " TEXT, " +
                    CATEGOTY_SPINNER_TODO + " TEXT, " +
                    DESCRIPTION_TODO + " TEXT, " +
                    START_DATE_TODO + " TEXT, " +
                    END_DATE_TODO  + " TEXT" + ");";
    //Функция для добавления новые
    public void addToDo(ToDo toDo){
        ContentValues values = new ContentValues();
        values.put(TITLE_TODO, toDo.getTitle());
        values.put(CATEGOTY_SPINNER_TODO, toDo.getCategory());
        values.put(DESCRIPTION_TODO, toDo.getDescription());
        values.put(START_DATE_TODO, toDo.getStart_date());
        values.put(END_DATE_TODO, toDo.getEnd_date());
        open();
        database.insert(TABLE_NAME,null,values);
        close();
    }
    public List<ToDo> getAllToDo() {
        List<ToDo> todoList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME;

        open();
        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
            ToDo toDo = new ToDo();
            toDo.setId(cursor.getInt(0));
            toDo.setTitle(cursor.getString(1));
            toDo.setCategory(cursor.getString(2));
            toDo.setDescription(cursor.getString(3));
            toDo.setStart_date(cursor.getString(4));
            toDo.setEnd_date(cursor.getString(5));
            todoList.add(toDo);
        }
        close();
        return todoList;
    }

    //Класс для создание и обновление базы данных
    public class DatabaseHelper extends SQLiteOpenHelper{

        //Конструктор
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //Создание базы данных
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);

        }

        //Upgrade базы данных
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
    public void updateToDo(ToDo toDo){
        ContentValues values = new ContentValues();
        values.put(TITLE_TODO,toDo.getTitle());
        values.put(CATEGOTY_SPINNER_TODO,toDo.getCategory());
        values.put(DESCRIPTION_TODO,toDo.getDescription());
        values.put(START_DATE_TODO,toDo.getStart_date());
        values.put(END_DATE_TODO,toDo.getEnd_date());
        open();
        database.update(TABLE_NAME, values, ID + "=" + toDo.getId(),null);
        close();
    }
    public void removeToDo(ToDo toDo){
        open();
        database.delete(TABLE_NAME,ID + "=?", new String[]{toDo.getId() + ""});

        close();
    }

}
