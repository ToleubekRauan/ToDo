package com.example.rauan.todo.controller;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rauan.todo.R;
import com.example.rauan.todo.database.Database;
import com.example.rauan.todo.model.ToDo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Rauan on 019 19.02.2017.
 */

public class ToDoAdapter extends BaseAdapter {
    private Context context;
    private Database db;
    private List<ToDo> toDoList;

    private class ViewHolder{
        TextView tvTitle;
        TextView tvCategory;
        TextView tvEndDate;
    }

    public ToDoAdapter(Context context, List<ToDo> toDoList) {
        this.context = context;
        this.toDoList = toDoList;
    }

    @Override
    public int getCount() {
        return toDoList.size();
    }

    @Override
    public Object getItem(int position) {
        return toDoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.todo_adapter_row, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView)convertView.findViewById(R.id.textViewAdapterTitle);
            holder.tvCategory = (TextView)convertView.findViewById(R.id.textViewAdapterCategory);
            holder.tvEndDate = (TextView)convertView.findViewById(R.id.textViewAdapterEndDate);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());






        holder.tvTitle.setText(toDoList.get(position).getTitle());
        holder.tvCategory.setText(toDoList.get(position).getCategory());
        holder.tvEndDate.setText(toDoList.get(position).getEnd_date());
        try {
            if(dateFormat.parse(holder.tvEndDate.getText().toString()).before(dateFormat.parse(date))){
                holder.tvTitle.setTextColor(Color.GREEN);
                holder.tvTitle.setText(toDoList.get(position).getTitle());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if(dateFormat.parse(holder.tvEndDate.getText().toString()).after(dateFormat.parse(date))){
                holder.tvTitle.setTextColor(Color.RED);
                holder.tvTitle.setText(toDoList.get(position).getTitle());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if(dateFormat.parse(holder.tvEndDate.getText().toString()).equals(dateFormat.parse(date))){
                holder.tvTitle.setTextColor(Color.YELLOW);
                holder.tvTitle.setText(toDoList.get(position).getTitle());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return convertView;

    }
}
