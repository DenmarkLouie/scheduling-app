package com.example.creoteccalendarapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register;
    private Button gobacktoLogin;

    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        gobacktoLogin = findViewById(R.id.gotologinpanel);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            startActivity(new Intent(RegisterActivity.this, StartActivity.class));
            finish();
        }
        else {
           //Do Nothing
        }
         gobacktoLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                  startActivity(new Intent(RegisterActivity.this, StartActivity.class));
                  finish();                                                                           
             }
         });
           
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_email = email.getText().toString();
                String text_password = password.getText().toString();

                if(TextUtils.isEmpty(text_email)|| TextUtils.isEmpty(text_password)){
                    Toast.makeText(RegisterActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                }
                else if (text_password.length() <6){
                    Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else {
                    
                    registerUser(text_email,text_password);

                }
            }
        });
    }
    //Register function
    private void registerUser (String email, String password){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){



                    Toast.makeText(RegisterActivity.this, "You have successfully registered your account!", Toast.LENGTH_SHORT).show();


                    startActivity(new Intent(RegisterActivity.this, UserDashboard.class));
                    finish();

                } else {
                    Toast.makeText(RegisterActivity.this, "Registration has failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}