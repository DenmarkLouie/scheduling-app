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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button gobacktoregister;
    private TextView forgotPass;

    ProgressDialog progressDialog;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        forgotPass = findViewById(R.id.forgotPassword);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);
        gobacktoregister = findViewById(R.id.gotoRegisterpanel);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
        else {
            //Do Nothing
        }

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, ForgotPassword.class));
                finish();
            }
        });


        gobacktoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_email = email.getText().toString();
                String text_password = password.getText().toString();

                if(TextUtils.isEmpty(text_email)|| TextUtils.isEmpty(text_password)){
                    Toast.makeText(StartActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setMessage("Logging you in....");
                    progressDialog.show();
                    signInUser(text_email,text_password);
                }
            }
        });
    }

    //Sign in function
    private void signInUser (String email, String password){

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //Logged in
                    Toast.makeText(StartActivity.this, "You are now Logged-in", Toast.LENGTH_SHORT).show();
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    finish();

                } else {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                        Toast.makeText(StartActivity.this, "Login has Failed, Please try again.", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

    }
}