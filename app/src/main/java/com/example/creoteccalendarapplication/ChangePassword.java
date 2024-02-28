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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

public class ChangePassword extends AppCompatActivity {

    private EditText currentpass;
    private EditText newPass;
    private EditText confirmNewPass;

    private Button goBack;
    private Button confirmChange;

    private FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_change_password);


        auth = FirebaseAuth.getInstance();


        currentpass = findViewById(R.id.currentPassword);
        newPass = findViewById(R.id.newPassword);
        confirmNewPass = findViewById(R.id.confirmNewPassword);

        goBack = findViewById(R.id.goBack);
        confirmChange = findViewById(R.id.confirmChangePass);



        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePassword.this, UserDashboard.class));
                finish();
            }
        });

        confirmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_currentPass = currentpass.getText().toString();
                String text_newpass = newPass.getText().toString();
                String text_confirmNewPass = confirmNewPass.getText().toString();

                String email = auth.getCurrentUser().getEmail();
                if(TextUtils.isEmpty(text_currentPass) || TextUtils.isEmpty(text_newpass) || TextUtils.isEmpty(text_confirmNewPass )){
                    Toast.makeText(ChangePassword.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                    currentpass.getText().clear();
                    newPass.getText().clear();
                    confirmNewPass.getText().clear();
                    }
                else {

                    if(newPass.getText().toString().equals(confirmNewPass.getText().toString())){
                        SignIn(email,text_currentPass,text_newpass);
                    }
                    else{
                        Toast.makeText(ChangePassword.this, "New Passwords do not match", Toast.LENGTH_SHORT).show();
                        currentpass.getText().clear();
                        newPass.getText().clear();
                        confirmNewPass.getText().clear();
                    }

                }

            }
        });

    }

    private void SignIn(String email, String currentPass, String newpass){

        auth.signInWithEmailAndPassword(email,currentPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    changePass(newpass);

                } else {

                    Toast.makeText(ChangePassword.this, "Something went wrong, Please try again later.", Toast.LENGTH_SHORT).show();
                    currentpass.getText().clear();
                    newPass.getText().clear();
                    confirmNewPass.getText().clear();
                }

            }
        });

    }

    private void changePass(String newPassword){

       FirebaseUser user = auth.getCurrentUser();

       user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               Toast.makeText(ChangePassword.this, "You have successfully changed your password.", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(ChangePassword.this, UserDashboard.class));
               finish();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(ChangePassword.this, "Something went wrong, Please try again later.", Toast.LENGTH_SHORT).show();
           }
       });
    }
}