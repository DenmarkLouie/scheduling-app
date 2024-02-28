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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgotPassword extends AppCompatActivity {

    private EditText email;
    private Button goBack;
    private Button confirmReset;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgot_password);


        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.EmailAdd);
        goBack = findViewById(R.id.goBack);
        confirmReset = findViewById(R.id.sendForgotPass);


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPassword.this, StartActivity.class));
                finish();
            }
        });


        confirmReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text_email = email.getText().toString();

                if(TextUtils.isEmpty(text_email)){
                    Toast.makeText(ForgotPassword.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                    sendPWReset(text_email);
                }

            }
        });

    }


    private void sendPWReset(String Email){

                auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ForgotPassword.this, "Password reset email has been sent!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPassword.this, StartActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPassword.this, "Something went wrong, Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}