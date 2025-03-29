package com.example.employeemanagementsystem2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
        fetchAttendance();
    }

    private void fetchAttendance() {
        db.collection("Attendance")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        attendanceList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Attendance attendance = document.toObject(Attendance.class);
                            attendanceList.add(attendance);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error Loading Details", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }
}
