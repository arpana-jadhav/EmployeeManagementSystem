package com.example.employeemanagementsystem2.EmployeAttendance;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagementsystem2.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ViewAttendanceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EmployeeAttendanceAdapter adapter;
    private List<Attendance> attendanceList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_attendance);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceList = new ArrayList<>();
        adapter = new EmployeeAttendanceAdapter(attendanceList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchAllUsersAttendance();
    }

    private void fetchAllUsersAttendance() {
        db.collection("users").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot userDoc : task.getResult()) {
                            String userId = userDoc.getId();
                            String userName = userDoc.getString("Employee Name");
                            String email = userDoc.getString("Email");

                            fetchUserAttendance(userId, userName, email);
                        }
                    } else {
                        Toast.makeText(this, "Error fetching users", Toast.LENGTH_SHORT).show();
                        Log.e("FirestoreDebug", "Error fetching users", task.getException());
                    }
                });
    }

    private void fetchUserAttendance(String userId, String userName, String email) {
        db.collection("Attendance").document(userId).collection("Record").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot attendanceDoc : task.getResult()) {
                            String date = attendanceDoc.getId();
                            String status = attendanceDoc.getString("status");
                            String timeIn = attendanceDoc.getString("checkInTime");
                            String timeOut = attendanceDoc.getString("checkOutTime");

                            Attendance attendance = new Attendance(userName, email, date, status, timeIn, timeOut);
                            attendanceList.add(attendance);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreDebug", "Error fetching attendance for " + userId, task.getException());
                    }
                });
    }
}
