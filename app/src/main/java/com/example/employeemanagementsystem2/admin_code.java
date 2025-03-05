package com.example.employeemanagementsystem2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class admin_code extends AppCompatActivity {
    TextInputEditText editTextName,editTextCode,editTextEmail,editTextPassword;
    Button buttonVerify;
    ProgressBar progressBar;
    TextView textView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_code);

        editTextName=findViewById(R.id.aname);
        editTextCode=findViewById(R.id.code);
        editTextEmail=findViewById(R.id.aemail);
        editTextPassword=findViewById(R.id.apassword);
        buttonVerify=findViewById(R.id.btn_verify);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.backtologin);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String aname, code,aemail,apassword;
                aname = String.valueOf(editTextName.getText());
                code = String.valueOf(editTextCode.getText());
                aemail = String.valueOf(editTextEmail.getText());
                apassword = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(aname)) {
                    Toast.makeText(admin_code.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(admin_code.this, "Enter Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(aemail)) {
                    Toast.makeText(admin_code.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(apassword)) {
                    Toast.makeText(admin_code.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }
}