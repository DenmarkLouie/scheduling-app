package com.example.creoteccalendarapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button gotoDashboard;
    private CalendarView calendar;
    private Button NewSched;
    private Spinner userSpinner;
    private Button search;

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayList<user> userArrayList;
    Adapter myAdapter;

    ValueEventListener listener;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseDatabase rtdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Data....");
        progressDialog.show();


        search = findViewById(R.id.SpinnerSearch);
        gotoDashboard = findViewById(R.id.gotodashboard);
        NewSched = findViewById(R.id.addSchedule);
        calendar = findViewById(R.id.Calendar);
        userSpinner = findViewById(R.id.spinnerUser);

        recyclerView = findViewById(R.id.RecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        rtdb = FirebaseDatabase.getInstance("https://creoteccalendarapp-default-rtdb.asia-southeast1.firebasedatabase.app/");

        String uid = auth.getUid();

        String date = new SimpleDateFormat("M-dd-yyyy", Locale.getDefault()).format(new Date());

        DatabaseReference myRef = rtdb.getReference("UserData").child("users");

       List<idcollection> userList = new ArrayList<>();

       ArrayAdapter<idcollection> adapter = new ArrayAdapter<idcollection>(this, android.R.layout.simple_spinner_item,userList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userSpinner.setAdapter(adapter);

        IDHandler.ID = uid;

        if (auth.getCurrentUser() != null) {



            //Fetch userdata from database.
            DocumentReference docRef = db.collection("userdata").document(uid);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {



                    if(task.isSuccessful()){

                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){

                            if(document.getData().get("userAccesslevel").toString().equals("Staff")) {
                                userSpinner.setVisibility(View.GONE);
                                search.setVisibility(View.GONE);

                            }

                            else {

                                listener=myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for(DataSnapshot mydata : snapshot.getChildren()){
                                            String name = mydata.child("name").getValue().toString();
                                            String id = mydata.getKey().toString();

                                            idcollection user1 = new idcollection(id, name);


                                            userList.add(user1);
                                            adapter.notifyDataSetChanged();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }



                            userArrayList = new ArrayList<user>();

                            myAdapter = new Adapter(MainActivity.this,userArrayList);

                            EventChangeListener(uid,date);

                            recyclerView.setAdapter(myAdapter);


                        }
                        else
                        {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }

                            startActivity(new Intent(MainActivity.this, UserDashboard.class));
                            finish();
                            //NO Data
                            Toast.makeText(MainActivity.this,  "Please provide your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        //fail
                    }
                }
            });


        }
        else {
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            finish();
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idcollection user = (idcollection) userSpinner.getSelectedItem();
               getUserID(user);

            }
        });



        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String Date
                        = (month + 1) + "-" + dayOfMonth + "-" + year;
                userArrayList.clear();
                myAdapter.notifyDataSetChanged();
                EventChangeListener(uid,Date);
            }

        });

        gotoDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, UserDashboard.class));
                finish();
            }
        });

        NewSched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewSchedule.class));
                finish();
            }
        });
    }

    private void EventChangeListener(String id, String Date) {


        db.collection("userdata").document(id).collection("usersched").whereEqualTo("ScheduleDate",Date).orderBy("ScheduleTime", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null) {
                    userArrayList.clear();
                    if(progressDialog.isShowing()){
                       progressDialog.dismiss();
                    }
                    Toast.makeText(MainActivity.this, "There has been an Error!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        userArrayList.add(dc.getDocument().toObject(user.class));
                    }
                    else {

                        if(progressDialog.isShowing()){
                           progressDialog.dismiss();
                        }

                         }

                    myAdapter.notifyDataSetChanged();

                }

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

            }
        });

    }

    private void getUserID(idcollection user){
        String date = new SimpleDateFormat("M-dd-yyyy", Locale.getDefault()).format(new Date());
        userArrayList.clear();
        myAdapter.notifyDataSetChanged();
        EventChangeListener(user.getId(),date);
        IDHandler.ID = user.getId();

    }

}