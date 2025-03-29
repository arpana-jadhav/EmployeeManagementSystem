package com.example.employeemanagementsystem2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextView txtEmployeeAttendance;
    private final String employeeId = "useId"; // Replace with dynamic employee ID

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        txtEmployeeAttendance = findViewById(R.id.txtEmployeeAttendance);
        db = FirebaseFirestore.getInstance();

        calculateTotalAttendance();
    }

    private void calculateTotalAttendance() {
        db.collection("Attendance").document(employeeId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the attendance list
                        List<String> daysPresent = (List<String>) documentSnapshot.get("days_present");

                        if (daysPresent != null) {
                            int totalDays = daysPresent.size(); // Calculate total attendance
                            txtEmployeeAttendance.setText("Total Days Attended: " + totalDays);
                        } else {
                            txtEmployeeAttendance.setText("No attendance records found");
                        }
                    } else {
                        txtEmployeeAttendance.setText("Employee not found");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching data", e));
    }
}
