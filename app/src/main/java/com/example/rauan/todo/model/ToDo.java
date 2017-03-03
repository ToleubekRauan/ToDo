package com.example.rauan.todo.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Rauan on 019 19.02.2017.
 */

public class ToDo implements Parcelable{
    private int id;
    private String title;
    private String category;
    private String description;
    private String start_date;
    private String end_date;
    private String photo;

    public ToDo(){

    }

    public ToDo(int id, String title, String category, String description, String start_date, String end_date) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
    public String getPhoto() {
        return photo;
    }

    //Dekodiruem stroku to baiti potom v bitmap
    //String -> byte[] -> bitmap
    public Bitmap getBitmapImage() {
        if (photo != null) {
            byte[] decodedBytes = Base64.decode(photo, 0);
            Bitmap resultBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            return resultBitmap;
        }
        else {
            return null;
        }
    }

    //NE ISPOLZOVIAT
    //Kodiruem foto v formate JPEG v String
    //bitmap -> byte[] -> String
    public void setPhoto(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
        this.photo = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
    }

    @Override
    public String toString() {
        return "ToDo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeString(start_date);
        dest.writeString(end_date);
        dest.writeString(photo);

    }
    public ToDo(Parcel in){
        id = in.readInt();
        title = in.readString();
        category = in.readString();
        description = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        photo = in.readString();
    }
    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new ToDo(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new ToDo[size];
        }
    };
}

