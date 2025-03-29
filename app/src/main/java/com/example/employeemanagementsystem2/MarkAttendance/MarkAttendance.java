package com.example.employeemanagementsystem2.MarkAttendance;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagementsystem2.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MarkAttendance extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;
    private List<Employee> employeeList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        recyclerView = findViewById(R.id.recyclerViewEmployees);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        employeeList = new ArrayList<>();
        adapter = new EmployeeAdapter(employeeList);
        recyclerView.setAdapter(adapter);

        fetchEmployees();
    }

    private void fetchEmployees() {
        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                if (document.exists()) {
                    String uid = document.getId();
                    String name = document.getString("Employee Name");
                    String email = document.getString("Email");

                    Log.d("Firestore", "Fetched: " + name + ", " + email);

                    Employee employee = new Employee(uid, name, email);
                    employeeList.add(employee);
                }
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error fetching data", e);
        });
    }

}
