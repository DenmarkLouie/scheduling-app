package com.example.creoteccalendarapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ScheduleDetails extends AppCompatActivity {

    private EditText ClientName;
    private EditText ClientSchool;
    private EditText ClientNumber;
    private EditText zoomLink;
    private TextView isDone;
    private TextView ProdTypeTxt;
    private TextView SchedTimeTxt;
    private TextView ScheduledDate;
    private TextView schedDateLabel;
    private Spinner ProductType;
    private Spinner Time;
    private TextView userDesignation;
    final Calendar myCalendar = Calendar.getInstance();
    private Button schedDate;
    private Button cancelButt;
    private Button editButt;
    private Button confirmReSched;
    private Button confirmEdit;
    private Button goHome;
    private Button addZoom;
    private Button confirmZoom;
    private Button setFinish;
    private Button setPostponed;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_schedule_details);

        schedDateLabel = findViewById(R.id.SchedDateLabel);
        ScheduledDate = findViewById(R.id.schedDate);
        ClientName = findViewById(R.id.clientName);
        ClientSchool = findViewById(R.id.clientSchool);
        ClientNumber = findViewById(R.id.clientNumber);
        zoomLink = findViewById(R.id.ZoomLink);
        isDone = findViewById(R.id.schedStatus);
        ProdTypeTxt = findViewById(R.id.ProductType);
        SchedTimeTxt = findViewById(R.id.SchedTIMETXT);

        userDesignation = findViewById(R.id.UserLevel);

        ProductType = findViewById(R.id.spinner);
        Time = findViewById(R.id.spinner2);


        setFinish = findViewById(R.id.FinishMeeting);
        setPostponed = findViewById(R.id.PostponeMeeting);

        addZoom = findViewById(R.id.EnterZoomLink);
        confirmZoom = findViewById(R.id.ConfirmZoomLink);
        confirmReSched = findViewById(R.id.ConfirmReSched);
        schedDate = findViewById(R.id.ReSchedule);
        cancelButt = findViewById(R.id.CancelButt);
        editButt = findViewById(R.id.editDoc);
        confirmEdit = findViewById(R.id.ConfirmEdit);
        goHome = findViewById(R.id.goHome);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ProductType.setVisibility(View.GONE);
        Time.setVisibility(View.GONE);
        ClientName.setEnabled(false);
        ClientSchool.setEnabled(false);
        ClientNumber.setEnabled(false);
        zoomLink.setEnabled(false);
        cancelButt.setVisibility(View.GONE);
        ScheduledDate.setVisibility(View.GONE);
        schedDateLabel.setVisibility(View.GONE);
        confirmReSched.setVisibility(View.GONE);
        confirmEdit.setVisibility(View.GONE);
        confirmZoom.setVisibility(View.GONE);
        addZoom.setVisibility(View.GONE);

        String id = auth.getCurrentUser().getUid();

        String idhandle = IDHandler.ID;

        setFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String DOCID = getIntent().getExtras().getString("ID");
                isFinished(idhandle,DOCID);

            }
        });

        setPostponed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DOCID = getIntent().getExtras().getString("ID");;
                isPostponed(idhandle,DOCID);
            }
        });


        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScheduleDetails.this, MainActivity.class));
                finish();
                IDHandler.ID = id;
            }
        });

        addZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomLink.setEnabled(true);
                cancelButt.setVisibility(View.VISIBLE);
                schedDate.setVisibility(View.GONE);
                editButt.setVisibility(View.GONE);
                confirmZoom.setVisibility(View.VISIBLE);
                setFinish.setVisibility(View.GONE);
                setPostponed.setVisibility(View.GONE);
                addZoom.setVisibility(View.GONE);
                zoomLink.requestFocus();
                addZoom.setVisibility(View.GONE);
            }
        });

        confirmZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String DOCID = getIntent().getExtras().getString("ID");
                String text_zoom = zoomLink.getText().toString();
                String userid = IDHandler.ID;
                updateZoom(userid, DOCID,text_zoom);

                }
            });

        cancelButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductType.setVisibility(View.GONE);
                Time.setVisibility(View.GONE);
                ClientName.setEnabled(false);
                ClientSchool.setEnabled(false);
                ClientNumber.setEnabled(false);
                cancelButt.setVisibility(View.GONE);
                schedDate.setVisibility(View.VISIBLE);
                editButt.setVisibility(View.VISIBLE);
                ScheduledDate.setVisibility(View.GONE);
                schedDateLabel.setVisibility(View.GONE);
                confirmReSched.setVisibility(View.GONE);
                ProdTypeTxt.setVisibility(View.VISIBLE);
                SchedTimeTxt.setVisibility(View.VISIBLE);
                confirmReSched.setVisibility(View.GONE);
                confirmEdit.setVisibility(View.GONE);
                confirmZoom.setVisibility(View.GONE);
                setFinish.setVisibility(View.VISIBLE);
                setPostponed.setVisibility(View.VISIBLE);
                zoomLink.setEnabled(false);

                if(userDesignation.getText().toString().equals("Manager")) {

                    addZoom.setVisibility(View.VISIBLE);

                }

            }
        });

        editButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductType.setVisibility(View.VISIBLE);
                Time.setVisibility(View.VISIBLE);
                ClientName.setEnabled(true);
                ClientSchool.setEnabled(true);
                ClientNumber.setEnabled(true);
                cancelButt.setVisibility(View.VISIBLE);
                schedDate.setVisibility(View.GONE);
                editButt.setVisibility(View.GONE);
                ProdTypeTxt.setVisibility(View.GONE);
                SchedTimeTxt.setVisibility(View.GONE);
                confirmEdit.setVisibility(View.VISIBLE);
                setFinish.setVisibility(View.GONE);
                setPostponed.setVisibility(View.GONE);
                addZoom.setVisibility(View.GONE);

            }
        });

        confirmReSched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text_status = isDone.getText().toString();
                String text_zoom = zoomLink.getText().toString();
                String text_Cname = ClientName.getText().toString();
                String text_schoolname = ClientSchool.getText().toString();
                String text_contactNum= ClientNumber.getText().toString();
                String text_date = ScheduledDate.getText().toString();
                String text_ProductType = ProdTypeTxt.getText().toString();
                String text_time = SchedTimeTxt.getText().toString();
                String DOCID = getIntent().getExtras().getString("ID");
                reSched(idhandle,text_Cname,text_schoolname,text_contactNum,text_date,text_ProductType,text_time,text_zoom,text_status);
                updateSchedule(idhandle,DOCID);

                startActivity(new Intent(ScheduleDetails.this, MainActivity.class));
                finish();
            }
        });

        confirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_status = isDone.getText().toString();
                String text_zoom = zoomLink.getText().toString();
                String text_Cname = ClientName.getText().toString();
                String text_schoolname = ClientSchool.getText().toString();
                String text_contactNum= ClientNumber.getText().toString();

                String text_ProductType = ProductType.getSelectedItem().toString();
                String text_time = Time.getSelectedItem().toString();
                String DOCID = getIntent().getExtras().getString("ID");

                editData(idhandle,DOCID,text_Cname,text_schoolname,text_contactNum,text_ProductType,text_time,text_zoom,text_status);
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
                ScheduledDate.setVisibility(View.VISIBLE);
                schedDateLabel.setVisibility(View.VISIBLE);
                editButt.setVisibility(View.GONE);
                cancelButt.setVisibility(View.VISIBLE);
                confirmReSched.setVisibility(View.VISIBLE);
                setFinish.setVisibility(View.GONE);
                setPostponed.setVisibility(View.GONE);
                addZoom.setVisibility(View.GONE);


            }
        };


        schedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ScheduleDetails.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String[] times = new String[]{"8:00 AM","9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM"};
        String[] prodtypes = new String[]{"WIP", "Robotics", "OJT","CreoLab"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, prodtypes);
        ProductType.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, times);
        Time.setAdapter(adapter2);



        if (auth.getCurrentUser() != null) {

            ClientName.setText(getIntent().getExtras().getString("ClientName"));
            ClientSchool.setText(getIntent().getExtras().getString("ClientSchool"));
            ClientNumber.setText(getIntent().getExtras().getString("ClientNumber"));
            ProdTypeTxt.setText(getIntent().getExtras().getString("ProductType"));
            SchedTimeTxt.setText(getIntent().getExtras().getString("ScheduleTime"));
            zoomLink.setText(getIntent().getExtras().getString("ZoomLink"));
            isDone.setText(getIntent().getExtras().getString("isDone"));


            DocumentReference docRef = db.collection("userdata").document(id);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){

                           userDesignation.setText(document.getData().get("userAccesslevel").toString());

                            if(document.getData().get("userAccesslevel").toString().equals("Manager")) {

                                addZoom.setVisibility(View.VISIBLE);

                            }
                            if(zoomLink.getText().toString().equals("NONE")){
                                setFinish.setVisibility(View.GONE);
                            }

                            if(isDone.getText().toString().equals("Finished")){
                                editButt.setVisibility(View.GONE);
                                setFinish.setVisibility(View.GONE);
                                setPostponed.setVisibility(View.GONE);
                                schedDate.setVisibility(View.GONE);
                                addZoom.setVisibility(View.GONE);
                            }

                            if(isDone.getText().toString().equals("Postponed")){
                                editButt.setVisibility(View.GONE);
                                setFinish.setVisibility(View.GONE);
                                setPostponed.setVisibility(View.GONE);
                                addZoom.setVisibility(View.GONE);
                            }

                            if(isDone.getText().toString().equals("Re-Scheduled")){
                                editButt.setVisibility(View.GONE);
                                setFinish.setVisibility(View.GONE);
                                setPostponed.setVisibility(View.GONE);
                                schedDate.setVisibility(View.GONE);
                                addZoom.setVisibility(View.GONE);
                            }
                        }
                    }
                    else{
                        //
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });



        }
        else {
            startActivity(new Intent(ScheduleDetails.this, StartActivity.class));
            finish();
        }
    }

    private void updateLabel() {

        String myFormat="M-dd-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        ScheduledDate.setText(dateFormat.format(myCalendar.getTime()));

    }
    private void editData (String id,String DocID, String clientname, String clientschool, String clientnum, String prodtype, String time, String ZoomLink, String isDone) {

        Map<String,Object> userData = new HashMap<>();
        userData.put("ClientName", clientname);
        userData.put("ClientSchool", clientschool);
        userData.put("ClientNumber", clientnum);
        userData.put("ProductType", prodtype);
        userData.put("ScheduleTime", time);
        userData.put("ZoomLink", ZoomLink);
        userData.put("isDone", isDone);


        db.collection("userdata").document(id).collection("usersched").document(DocID).update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(ScheduleDetails.this, "Meeting Details are now updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ScheduleDetails.this, MainActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ScheduleDetails.this, "Document Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateSchedule(String id, String docID){
        db.collection("userdata").document(id).collection("usersched").document(docID).update("isDone","Re-Scheduled").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ScheduleDetails.this, "Document Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void reSched (String id, String clientname, String clientschool, String clientnum, String date, String prodtype, String time, String ZoomLink, String isDone){

        Map<String,Object> userData = new HashMap<>();
        userData.put("ClientName", clientname);
        userData.put("ClientSchool", clientschool);
        userData.put("ClientNumber", clientnum);
        userData.put("ProductType", prodtype);
        userData.put("ScheduleDate", date);
        userData.put("ScheduleTime", time);
        userData.put("ZoomLink", ZoomLink);
        userData.put("isDone", isDone);


        db.collection("userdata").document(id).collection("usersched").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(ScheduleDetails.this, "A new schedule has been created!", Toast.LENGTH_SHORT).show();
                String ID = documentReference.getId();

                db.collection("userdata").document(id).collection("usersched").document(ID).update("ID",ID).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ScheduleDetails.this, "Document Error", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScheduleDetails.this, "Cannot add schedule please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void isFinished(String id, String docID){
        db.collection("userdata").document(id).collection("usersched").document(docID).update("isDone","Finished").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(ScheduleDetails.this, "Meeting marked as finished", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ScheduleDetails.this, MainActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ScheduleDetails.this, "Document Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void isPostponed(String id, String docID){
        db.collection("userdata").document(id).collection("usersched").document(docID).update("isDone","Finished").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(ScheduleDetails.this, "Meeting marked as postponed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ScheduleDetails.this, MainActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ScheduleDetails.this, "Document Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateZoom(String id, String docID,String Link){
        db.collection("userdata").document(id).collection("usersched").document(docID).update("ZoomLink",Link).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(ScheduleDetails.this, "Added new Zoom Link", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ScheduleDetails.this, MainActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ScheduleDetails.this, "Document Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
