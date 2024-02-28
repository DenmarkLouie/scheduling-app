package com.example.creoteccalendarapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class UserDashboard extends AppCompatActivity {

    private EditText FName;
    private EditText SName;
    private EditText CNum;
    private EditText CAdd;
    ProgressDialog progressDialog;
    private Button logout;
    private Button gotoMain;
    private Button Update;
    private Button Cancel;
    private Button saveUpdate;
    private TextView userAccess;
    private TextView changePass;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseDatabase rtdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_dashboard);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading User Data....");
        progressDialog.show();

        FName = findViewById(R.id.FullName);
        SName = findViewById(R.id.SchoolName);
        CNum = findViewById(R.id.ContactNum);
        CAdd = findViewById(R.id.ContactAdd);
        Update = findViewById(R.id.UpdateButton);
        saveUpdate = findViewById(R.id.SaveUpdate);
        Cancel = findViewById(R.id.CancelUpdate);
        logout = findViewById(R.id.logout);
        gotoMain = findViewById(R.id.gotoMain);
        userAccess = findViewById(R.id.accesslevel);
        changePass = findViewById(R.id.changePassword);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        rtdb = FirebaseDatabase.getInstance("https://creoteccalendarapp-default-rtdb.asia-southeast1.firebasedatabase.app/");


        String id = auth.getCurrentUser().getUid();

        gotoMain.setVisibility(View.GONE);

        if (auth.getCurrentUser() != null) {
            //Fetch userdata from database.
            DocumentReference docRef = db.collection("userdata").document(id);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){

                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            //Show Data
                            FName.setText(document.getData().get("Full Name").toString());
                            SName.setText(document.getData().get("School Name").toString());
                            CNum.setText(document.getData().get("Contact Number").toString());
                            CAdd.setText(document.getData().get("Address").toString());
                            userAccess.setText(document.getData().get("userAccesslevel").toString());
                            gotoMain.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }


                            //NO Data
                            Toast.makeText(UserDashboard.this,  "Please provide your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        //fail
                    }
                }
            });


        }
        else {
            startActivity(new Intent(UserDashboard.this, StartActivity.class));
            finish();
        }

        //Disabled
        FName.setEnabled(false);
        SName.setEnabled(false);
        CNum.setEnabled(false);
        CAdd.setEnabled(false);

        saveUpdate.setVisibility(View.GONE);
        Cancel.setVisibility(View.GONE);

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDashboard.this, ChangePassword.class));
                finish();
            }
        });


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUpdate.setVisibility(View.VISIBLE);
                Cancel.setVisibility(View.VISIBLE);
                Update.setVisibility(View.GONE);
                FName.setEnabled(true);
                SName.setEnabled(true);
                CNum.setEnabled(true);
                CAdd.setEnabled(true);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update.setVisibility(View.VISIBLE);
                FName.setEnabled(false);
                SName.setEnabled(false);
                CNum.setEnabled(false);
                CAdd.setEnabled(false);
                saveUpdate.setVisibility(View.GONE);
                Cancel.setVisibility(View.GONE);
            }
        });

        saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_name = FName.getText().toString();
                String text_schoolname = SName.getText().toString();
                String text_contactNum= CNum.getText().toString();
                String text_contactAdd = CAdd.getText().toString();
                String text_userAccess = userAccess.getText().toString();

                if(TextUtils.isEmpty(text_name)|| TextUtils.isEmpty(text_schoolname) || TextUtils.isEmpty(text_contactNum) || TextUtils.isEmpty(text_contactAdd)){
                    Toast.makeText(UserDashboard.this, "Cannot Save with Incomplete Credentials", Toast.LENGTH_SHORT).show();
                }
               else {

                    SaveUserDatatoDB(id,text_name,text_schoolname,text_contactNum,text_contactAdd,text_userAccess);

                }
            }
        });

        gotoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDashboard.this, MainActivity.class));
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();

                Toast.makeText(UserDashboard.this, "you have logged out.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserDashboard.this, StartActivity.class));
                finish();
            }
        });


    }

    private void SaveUserDatatoDB (String id, String name, String schoolname, String ContactNum, String ContactAdd, String userAccess)
    {
        Map<String,Object> userData = new HashMap<>();
        userData.put("Full Name", name);
        userData.put("School Name", schoolname);
        userData.put("Contact Number", ContactNum);
        userData.put("Address", ContactAdd);
        userData.put("userAccesslevel", userAccess);

        db.collection("userdata").document(id).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UserDashboard.this, "You have updated your user info!", Toast.LENGTH_SHORT).show();
                Update.setVisibility(View.VISIBLE);
                FName.setEnabled(false);
                SName.setEnabled(false);
                CNum.setEnabled(false);
                CAdd.setEnabled(false);
                saveUpdate.setVisibility(View.GONE);
                Cancel.setVisibility(View.GONE);
                gotoMain.setVisibility(View.VISIBLE);

                DatabaseReference myRef = rtdb.getReference("UserData");


                myRef.child("users").child(id).child("name").setValue(name);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserDashboard.this, "Failed to update user info!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}