package com.example.employeemanagementsystem2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PersonalDetails extends AppCompatActivity {

    private TextInputEditText editName, editCollege, editEmailAddress, editPhone;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details); // Ensure this matches your XML file name

        db = FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();

        editName = findViewById(R.id.editName);
        editCollege = findViewById(R.id.editCollege);
        editEmailAddress = findViewById(R.id.editEmailAddress);
        editPhone = findViewById(R.id.editPhone);
        progressBar = findViewById(R.id.progressBar);

        fetchEmployeeDetails();
    }

    private void fetchEmployeeDetails() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("users").document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String name = document.getString("Employee Name");
                            String contact = document.getString("Contact");
                            String email = document.getString("Email");
                            String address = document.getString("Address");

                            editName.setText(name);
                            editCollege.setText(contact);
                            editEmailAddress.setText(email);
                            editPhone.setText(address);
                        } else {
                            Toast.makeText(PersonalDetails.this, "No data found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("Firestore", "Error fetching document", task.getException());
                        Toast.makeText(PersonalDetails.this, "Error fetching details!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
