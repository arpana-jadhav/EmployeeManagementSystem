package com.example.employeemanagementsystem2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SalaryActivity extends AppCompatActivity {
        private FirebaseFirestore db;
        private TextView txtEmployeeSalary;
        private final String employeeId = "userId";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_salary);

            txtEmployeeSalary = findViewById(R.id.txtEmployeeSalary);
            db = FirebaseFirestore.getInstance();

            calculateSalary();
        }

        private void calculateSalary() {
            db.collection("Attendance").document(employeeId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve attendance data
                            long totalDaysPresent = documentSnapshot.getLong("total_days_present");
                            long salaryPerDay = documentSnapshot.getLong("salary_per_day");

                            // Calculate salary
                            long totalSalary = totalDaysPresent * salaryPerDay;

                            // Display salary
                            txtEmployeeSalary.setText("Total Salary: ₹" + totalSalary);
                        } else {
                            txtEmployeeSalary.setText("Employee not found");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error fetching data", e));
        }
}
