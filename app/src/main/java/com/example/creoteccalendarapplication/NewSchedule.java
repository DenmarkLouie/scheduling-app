package com.example.creoteccalendarapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;

public class NewSchedule extends AppCompatActivity {
    private Button Home;
    private Button addSched;
    private Spinner ProductType;
    private Spinner Time;
    private EditText clientname;
    private EditText clientnum;
    private EditText clientschool;
    private EditText schedDate;
    final Calendar myCalendar = Calendar.getInstance();

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_schedule);

        Home = findViewById(R.id.gotoMain);
        addSched = findViewById(R.id.addSched);
        ProductType = findViewById(R.id.spinner);
        clientname = findViewById(R.id.FullName);
        clientnum = findViewById(R.id.ContactNum);
        clientschool = findViewById(R.id.SchoolName);
        Time = findViewById(R.id.spinner2);
        schedDate = findViewById(R.id.DateSched);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String id = auth.getCurrentUser().getUid();


        if (auth.getCurrentUser() != null) {
            //Do Nothing
        }
        else {
            startActivity(new Intent(NewSchedule.this, StartActivity.class));
            finish();
        }

        String[] times = new String[]{"8:00 AM","9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM"};
        String[] prodtypes = new String[]{"WIP", "Robotics", "OJT","CreoLab"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, prodtypes);
       ProductType.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, times);
        Time.setAdapter(adapter2);

        schedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NewSchedule.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewSchedule.this, MainActivity.class));
                finish();
            }
        });

        addSched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text_name = clientname.getText().toString();
                String text_schoolname = clientschool.getText().toString();
                String text_contactNum= clientnum.getText().toString();
                String text_date = schedDate.getText().toString();
                String text_ProductType = ProductType.getSelectedItem().toString();
                String text_time = Time.getSelectedItem().toString();

                if(TextUtils.isEmpty(text_name)|| TextUtils.isEmpty(text_schoolname) || TextUtils.isEmpty(text_contactNum) || TextUtils.isEmpty(text_date)){
                    Toast.makeText(NewSchedule.this, "Cannot add schedule with incomplete credentials", Toast.LENGTH_SHORT).show();
                }
                else {

                    saveSchedtoDB(id,text_name,text_schoolname,text_contactNum,text_date,text_ProductType,text_time);

                }

            }
        });

    }

    private void saveSchedtoDB (String id, String clientname, String clientschool, String clientnum, String date, String prodtype, String time){

        Map<String,Object> userData = new HashMap<>();
        userData.put("ClientName", clientname);
        userData.put("ClientSchool", clientschool);
        userData.put("ClientNumber", clientnum);
        userData.put("ProductType", prodtype);
        userData.put("ScheduleDate", date);
        userData.put("ScheduleTime", time);
        userData.put("ZoomLink", "NONE");
        userData.put("isDone", "Pending");


        db.collection("userdata").document(id).collection("usersched").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(NewSchedule.this, "A new schedule has been created!", Toast.LENGTH_SHORT).show();
                String ID = documentReference.getId();
                db.collection("userdata").document(id).collection("usersched").document(ID).update("ID",ID).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                        startActivity(new Intent(NewSchedule.this, MainActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(NewSchedule.this, "Document Error", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewSchedule.this, "Cannot add schedule please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLabel(){
        String myFormat="M-dd-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        schedDate.setText(dateFormat.format(myCalendar.getTime()));
    }
}