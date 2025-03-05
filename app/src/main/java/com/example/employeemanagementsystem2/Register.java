package com.example.employeemanagementsystem2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    TextInputEditText editTextName;
    TextInputEditText editTextContact;
    TextInputEditText editTextEmail;
    TextInputEditText editTextAddress;
    TextInputEditText editTextPassword;

    TextInputEditText editTextConfirmPassword;

    //TextInputEditText editTextEmail,editTextPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseFirestore fstore;
    String uid="";
    String userId;
    TextView textView;
    FirebaseUser currentUser;

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        mAuth= FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        editTextName=findViewById(R.id.name);
        editTextContact=findViewById(R.id.contact);
        editTextEmail=findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        editTextConfirmPassword=findViewById(R.id.cpassword);
        editTextAddress=findViewById(R.id.address);
        buttonReg=findViewById(R.id.btn_register);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.loginNow);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();

            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String name, contact, email, address,password,cpassword;
                //String email,password;
                name = String.valueOf(editTextName.getText());
                contact = String.valueOf(editTextContact.getText());
                email = String.valueOf(editTextEmail.getText());
                address = String.valueOf(editTextAddress.getText());
                password=String.valueOf(editTextPassword.getText());
                cpassword=String.valueOf(editTextConfirmPassword.getText());

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(Register.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(contact)) {
                    Toast.makeText(Register.this, "Enter Contact Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                   Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length()<5){
                    Toast.makeText(Register.this, "Enter Password must be greater than 5", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(cpassword)) {
                    Toast.makeText(Register.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(cpassword)){
                    Toast.makeText(Register.this, "Password is Not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(Register.this, "Enter Address", Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "You are Registered Successfully",
                                            Toast.LENGTH_SHORT).show();
                                    userId=mAuth.getCurrentUser().getUid();
                                    Log.d(TAG, "onComplete uid : "+userId);
                                    DocumentReference documentReference=fstore.collection("users").document(userId);
                                    Map<String,Object>user=new HashMap<>();
                                    user.put("Employee Name",name);
                                    user.put("Contact",contact);
                                    user.put("Email",email);
                                    user.put("Password",password);
                                    user.put("ConfirmPassword",cpassword);
                                    user.put("Address",address);
                                    user.put("Role","User");
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: user Profile is created for"+userId);
                                        }
                                    });
                                    mAuth.signOut();
                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Register failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}