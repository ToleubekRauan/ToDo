package com.example.rauan.todo.controller;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.rauan.todo.R;
import com.example.rauan.todo.database.Database;
import com.example.rauan.todo.model.ToDo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToDoActivity extends AppCompatActivity {
    Spinner spinner;
    EditText editTextTitleToDoInput;
    EditText editTextDescriptionToDoInput;
    EditText editTextStartDateToDoInput;
    EditText editTextEndDateToDoInput;
    Button buttonSaveNewToDo;

    private static final int CAMERA_PERMISSION_REQUEST = 300;
    private static final int GALLERY_PERMISSION_REQUEST = 301;

    private static final int CAMERA_REQUEST = 400;
    private static final int GALLERY_REQUEST = 401;

    private Bitmap bitmapUserAvatar;

    private String imageRealPath;
    private ImageView ivAvatar;

    private CreateOrUpdateToDoAsync createOrUpdateToDoAsync;
    private ToDo oldOrNewToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        addItemOnSpinner();
        editTextTitleToDoInput = (EditText) findViewById(R.id.editTextTitleToDo);
        editTextStartDateToDoInput = (EditText) findViewById(R.id.editTextStartDateToDo);
        editTextEndDateToDoInput = (EditText) findViewById(R.id.editTextEndDateToDo);
        editTextDescriptionToDoInput = (EditText) findViewById(R.id.editTextDescriptionToDo);
        editTextStartDateToDoInput = (EditText) findViewById(R.id.editTextStartDateToDo);
        editTextEndDateToDoInput = (EditText) findViewById(R.id.editTextEndDateToDo);
        spinner = (Spinner) findViewById(R.id.spinner);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        editTextStartDateToDoInput.addTextChangedListener(new TextWatcher() {
            int length_start_date = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                length_start_date = editTextStartDateToDoInput.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if((length_start_date < length) && (length == 2 || length ==5)){
                    s.append("/");
                }

            }

        });
        //Добавить / в dd/mm/yyyy
        editTextEndDateToDoInput.addTextChangedListener(new TextWatcher() {
            int length_start_date = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                length_start_date = editTextEndDateToDoInput.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if((length_start_date < length) && (length == 2 || length ==5)){
                    s.append("/");
                }

            }
        });
        buttonSaveNewToDo = (Button) findViewById(R.id.buttonSaveToDo);
        buttonSaveNewToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitleToDoInput.getText().toString();
                String category = spinner.getSelectedItem().toString();
                String description = editTextDescriptionToDoInput.getText().toString();
                String start_date = editTextStartDateToDoInput.getText().toString();
                String end_date = editTextEndDateToDoInput.getText().toString();
                createOrUpdateToDoAsync = new CreateOrUpdateToDoAsync(title,category,description,start_date,end_date);
                createOrUpdateToDoAsync.execute();
            }
        });
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isVersion23()) {
                    getCameraAndGalleyPermission();
                }
                else {
                    avatarDialog();
                }

                //avatarDialog();
            }
        });
        if (getIntent().getParcelableExtra("todo_entity") != null) {
            oldOrNewToDo = (ToDo) getIntent()
                    .getParcelableExtra("todo_entity");

            //Zapolniaem polia dannimi kontakta
            editTextTitleToDoInput.setText(oldOrNewToDo.getTitle());
            //spinner.getSelectedItem(oldOrNewToDo.getCategory().);
            editTextDescriptionToDoInput.setText(oldOrNewToDo.getDescription());
            editTextStartDateToDoInput.setText(oldOrNewToDo.getStart_date());
            editTextEndDateToDoInput.setText(oldOrNewToDo.getEnd_date());
            if (oldOrNewToDo.getPhoto() != null) {
                ivAvatar.setImageURI(Uri.parse(oldOrNewToDo.getPhoto()));
            Log.d("Contact_get_intent", getIntent().getParcelableExtra("todo_entity").toString());

        }

        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        String strTime = simpleDateFormat.format(new Date());
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(editTextEndDateToDoInput.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date1.after(date2)) {
            editTextTitleToDoInput.setTextColor(Color.RED);
        }

        if (date1.before(date2)) {
            editTextTitleToDoInput.setTextColor(Color.GREEN);
        }*/



    }
    }
    public void avatarDialog() {

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[]{"Camera", "Gallery"});

        AlertDialog avatarDialog =
                new AlertDialog.Builder(this)
                        .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {

                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, CAMERA_REQUEST);

//                                if (isVersion23() == true) {
//                                    getCameraPermission();
//                                }
//                                else {
//
//                                }
                                }
                                else if (which == 1) {

                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, GALLERY_REQUEST);


//                                if (isVersion23() == true) {
//                                    getGalleryPermission();
//                                }
//                                else {
//                                    Intent intent = new Intent(Intent.ACTION_PICK);
//                                    startActivityForResult(intent, GALLERY_REQUEST);
//                                }
                                }
                            }
                        })
                        .create();

        avatarDialog.show();
    }

    private boolean isVersion23() {
        return Build.VERSION.SDK_INT >= 23;
    }


    private void getCameraAndGalleyPermission() {

        boolean hasCameraAndGalleyPermission =
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED;

        if (hasCameraAndGalleyPermission == true) {
            avatarDialog();
        }
        else {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    CAMERA_PERMISSION_REQUEST);
        }
    }

    private void getCameraPermission() {

        boolean hasCameraPermission =
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED;

        if (hasCameraPermission == true) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
        else {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    CAMERA_PERMISSION_REQUEST);
        }
    }

    private void getGalleryPermission() {
        boolean hasGalleryPermission =
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                        == PackageManager.PERMISSION_GRANTED;


        if (hasGalleryPermission == true) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_REQUEST);
        }
        else {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSION_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<String> stringList = new ArrayList<String>(Arrays.asList(permissions));
        List<Integer> integerList = new ArrayList<Integer>();

        for (int i = 0; i < grantResults.length; i++) {
            integerList.add(grantResults[i]);
        }


        Log.d("My_camera_permission", requestCode +
                "\n" + stringList.toString() +
                "\n" + integerList.toString());

        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            avatarDialog();
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(intent, CAMERA_REQUEST);
        }
        else {
//            requestPermissions(
//                    new String[]{Manifest.permission.CAMERA,
//                            Manifest.permission.READ_EXTERNAL_STORAGE},
//                    CAMERA_PERMISSION_REQUEST);
        }

//        if (requestCode == GALLERY_PERMISSION_REQUEST) {
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            startActivityForResult(intent, GALLERY_REQUEST);
//        }
//        else {
//            requestPermissions(
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    GALLERY_PERMISSION_REQUEST);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Log.d("Camera_result_data", data.getExtras().get("data").toString());
                bitmapUserAvatar = (Bitmap) data.getExtras().get("data");
                ivAvatar.setImageBitmap(bitmapUserAvatar);

                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(),
                        bitmapUserAvatar, "avatar", "desc");

                imageRealPath = getRealPath(path);

                Log.d("Image_camera_path", path + "\n" + getRealPath(path));


            }
            else if (requestCode == GALLERY_REQUEST) {
                if (data != null) {
                    Log.d("Gallery_result_data", data.getData().toString());

                    try {
                        bitmapUserAvatar = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                data.getData());
                        ivAvatar.setImageBitmap(bitmapUserAvatar);

                        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(),
                                bitmapUserAvatar, "avatar", "desc");

                        imageRealPath = getRealPath(path);

                        Log.d("Image_camera_path", MediaStore.Images.Media.insertImage(this.getContentResolver(),
                                bitmapUserAvatar, "avatar", "desc"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                Log.d("Gallery_result_data", "Null");
            }
        }
    }


    private String getRealPath(String path) {
        Cursor cursor = getContentResolver().query(Uri.parse(path), null, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(index);
    }
    public void addItemOnSpinner(){
        spinner = (Spinner) findViewById(R.id.spinner);
        List<String> spinnerList = new ArrayList<>();
        spinnerList.add("Важно");
        spinnerList.add("Неважно");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerList);
        spinner.setAdapter(arrayAdapter);

    }
    public class CreateOrUpdateToDoAsync extends AsyncTask<Void,Void,Void>{
        private String title,category,description,start_date,end_date;

        public CreateOrUpdateToDoAsync(String title, String category, String description, String start_date, String end_date) {
            this.title = title;
            this.category = category;
            this.description = description;
            this.start_date = start_date;
            this.end_date = end_date;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Database database = new Database(ToDoActivity.this);
            //Log.d("Contact_get_intent", getIntent().getParcelableExtra("contact_entity").toString());

            if(oldOrNewToDo != null){
                oldOrNewToDo.setTitle(title);
                oldOrNewToDo.setCategory(category);
                oldOrNewToDo.setDescription(description);
                oldOrNewToDo.setStart_date(start_date);
                oldOrNewToDo.setEnd_date(end_date);
                database.updateToDo(oldOrNewToDo);
            }else{
                ToDo toDo = new ToDo();
                toDo.setTitle(title);
                toDo.setCategory(category);
                toDo.setDescription(description);
                toDo.setStart_date(start_date);
                toDo.setEnd_date(end_date);
                database.addToDo(toDo);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }







}
