package com.example.employeemanagementsystem2;

import static android.app.ProgressDialog.show;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private Button btn_forgetpassword;
    private EditText textEmail;
    private String email;
    private FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);

        auth=FirebaseAuth.getInstance();

        btn_forgetpassword=findViewById(R.id.btn_forgetpassword);

        btn_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                validateData();
            }

            private void validateData() {
                email=textEmail.getText().toString();
                if(email.isEmpty()){
                    textEmail.setError("Eter Email");
                }else{
                    forgetPassword();
                }
            }

            private void forgetPassword() {
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgetPassword.this, "Check your Email", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(ForgetPassword.this, "Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}