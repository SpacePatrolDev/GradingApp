package com.example.gradingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Declaring all the variables
    String course = "";
    int credit = 0;
    String courseList[] = {"PROG 8080","PROG 8081","PROG 8082"};

    //Declaring all the views
    ListView courses;
    EditText Txt_StudentID, Txt_FirstName, Txt_LastName, Txt_Marks;
    Button Btn_AddData, Btn_ViewData, Btn_UpdateData, Btn_DeleteData, Btn_SearchByID, Btn_SearchByProg;
    RadioGroup Grp_Credit;

    //Declaring Database Handler
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialing all the variables
        courses = findViewById(R.id.course);
        Txt_StudentID = findViewById(R.id.Student_id);
        Txt_FirstName = findViewById(R.id.Student_FName);
        Txt_LastName = findViewById(R.id.Student_LName);
        Txt_Marks = findViewById(R.id.Student_Marks);

        Btn_AddData = findViewById(R.id.btn_add_data);
        Btn_ViewData = findViewById(R.id.btn_view_data);
        Btn_UpdateData = findViewById(R.id.btn_update_data);
        Btn_DeleteData = findViewById(R.id.btn_delete_data);
        Btn_SearchByID = findViewById(R.id.btn_search_id);
        Btn_SearchByProg = findViewById(R.id.btn_search_prog);

        Grp_Credit = findViewById(R.id.rdgrp_credit);

        databaseHandler = new DatabaseHandler(this);

        //Setting adaptor for ListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.activity_listview,
                R.id.txt_select_course, courseList);
        courses.setAdapter(arrayAdapter);

        //Getting selected item from ListView
        courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0: course = "PROG 8080";
                            break;

                    case 1: course = "PROG 8081";
                            break;

                    case 2: course = "PROG 8082";
                            break;
                }
            }
        });

        //Getting selected item from Radio Group
        Grp_Credit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.rdbtn_2: credit = 2;
                                        break;
                    case R.id.rdbtn_3: credit = 3;
                                        break;
                    case R.id.rdbtn_4: credit = 4;
                                        break;
                }
            }
        });

        //This function will Add data to the database
        Btn_AddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Condition to validate all the data is correct
                if(!(Txt_FirstName.getText().toString().equals("")) &&
                        !(Txt_LastName.getText().toString().equals("")) &&
                        !(Txt_Marks.getText().toString().equals("")) &&
                        !(course.equals("")) &&
                        credit > 1 && credit < 5 )
                {
                    ContentValues cv = new ContentValues();
                    cv.put("FName", Txt_FirstName.getText().toString());
                    cv.put("LName", Txt_LastName.getText().toString());
                    cv.put("Marks", Txt_Marks.getText().toString());
                    cv.put("Course", course);
                    cv.put("Credit", credit);

                    boolean result = databaseHandler.AddData(cv);

                    //Condition to check if database was updated successfully
                    if (result) {
                        Toast.makeText(MainActivity.this, "Record Added Successfully", Toast.LENGTH_LONG).show();
                        clearAll();
                    } else {
                        Toast.makeText(MainActivity.this, "Oops! Data not added", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Some input is missing!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //This function will show all the data present in the Database
        Btn_ViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Msg = "";
                Cursor cursor = databaseHandler.getAllData();

                //Check if the data is present in database
                if(cursor.getCount() > 0)
                {
                    while (cursor.moveToNext())
                    {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("ID : "+cursor.getInt(0)+"\n");
                        stringBuilder.append("First Name : "+cursor.getString(1)+"\n");
                        stringBuilder.append("Last Name : "+cursor.getString(2)+"\n");
                        stringBuilder.append("Marks : "+cursor.getString(3)+"\n");
                        stringBuilder.append("Program Code : "+cursor.getString(4)+"\n");
                        stringBuilder.append("Credit : "+cursor.getInt(5)+"\n");

                         Msg += stringBuilder.toString() + "\n";
                    }

                    showDialogBox("Student Table", Msg);
                }
                else
                {
                    showDialogBox("Data","No Data Available");
                }
            }
        });

        //This function will update the current record in database
        Btn_UpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(Txt_StudentID.getText().toString().equals(""))) {
                    Cursor cursor = databaseHandler.getDataByID(Txt_StudentID.getText().toString());

                    if(cursor.getCount() > 0)  {

                        if (!(Txt_FirstName.getText().toString().equals("")) &&
                                !(Txt_LastName.getText().toString().equals("")) &&
                                !(Txt_Marks.getText().toString().equals("")) &&
                                !(course.equals("")) &&
                                credit > 1 && credit < 5) {
                            ContentValues cv = new ContentValues();
                            cv.put("FName", Txt_FirstName.getText().toString());
                            cv.put("LName", Txt_LastName.getText().toString());
                            cv.put("Marks", Txt_Marks.getText().toString());
                            cv.put("Course", course);
                            cv.put("Credit", credit);

                            boolean result = databaseHandler.UpdateData(cv,Txt_StudentID.getText().toString());

                            if (result) {
                                Toast.makeText(MainActivity.this, "Record Updated Successfully", Toast.LENGTH_LONG).show();
                                clearAll();
                            } else {
                                Toast.makeText(MainActivity.this, "Oops! Data not added", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Some input is missing!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Record Not Found!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Enter ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //This function will delete the current record in the database
        Btn_DeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(Txt_StudentID.getText().toString().equals(""))) {
                    Cursor cursor = databaseHandler.getDataByID(Txt_StudentID.getText().toString());

                    if(cursor.getCount() > 0)  {
                            boolean result = databaseHandler.DeleteData(Txt_StudentID.getText().toString());

                            if (result) {
                                Toast.makeText(MainActivity.this, "Record Deleted Successfully", Toast.LENGTH_LONG).show();
                                clearAll();
                            } else {
                                Toast.makeText(MainActivity.this, "Oops! Data not added", Toast.LENGTH_SHORT).show();
                            }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Record Not Found!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Enter ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //This function will find the record based on the given ID
        Btn_SearchByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(Txt_StudentID.getText().toString().equals(""))) {
                    Cursor cursor = databaseHandler.getDataByID(Txt_StudentID.getText().toString());

                    if(cursor.getCount() > 0)  {
                        String Msg = "";
                        while (cursor.moveToNext())
                        {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("ID : "+cursor.getInt(0)+"\n");
                            stringBuilder.append("First Name : "+cursor.getString(1)+"\n");
                            stringBuilder.append("Last Name : "+cursor.getString(2)+"\n");
                            stringBuilder.append("Marks : "+cursor.getString(3)+"\n");
                            stringBuilder.append("Program Code : "+cursor.getString(4)+"\n");
                            stringBuilder.append("Credit : "+cursor.getInt(5)+"\n");

                            Msg += stringBuilder.toString() + "\n";
                        }
                        showDialogBox("Student Record", Msg);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Record Not Found!", Toast.LENGTH_SHORT).show();
                    }
                }
                else  {
                    Toast.makeText(MainActivity.this, "Enter ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //This function will find the record based to the selected course
        Btn_SearchByProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(course.equals(""))) {
                    Cursor cursor = databaseHandler.getDataByProg(course);

                    if(cursor.getCount() > 0)  {
                        String Msg = "";
                        while (cursor.moveToNext())
                        {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("ID : "+cursor.getInt(0)+"\n");
                            stringBuilder.append("First Name : "+cursor.getString(1)+"\n");
                            stringBuilder.append("Last Name : "+cursor.getString(2)+"\n");
                            stringBuilder.append("Marks : "+cursor.getString(3)+"\n");
                            stringBuilder.append("Program Code : "+cursor.getString(4)+"\n");
                            stringBuilder.append("Credit : "+cursor.getInt(5)+"\n");

                            Msg += stringBuilder.toString() + "\n";
                        }
                        showDialogBox("Student Record", Msg);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Record Not Found!", Toast.LENGTH_SHORT).show();
                    }
                }
                else  {
                    Toast.makeText(MainActivity.this, "Select Course", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //This function will create a dialog box
    public void showDialogBox(String title, String Msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(Msg);
        builder.setPositiveButton("EXIT", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //This function will erase all the data from views
    public void clearAll()
    {
        Txt_StudentID.setText("");
        Txt_FirstName.setText("");
        Txt_LastName.setText("");
        Txt_Marks.setText("");
        courses.clearChoices();
        Grp_Credit.clearCheck();
    }
}
