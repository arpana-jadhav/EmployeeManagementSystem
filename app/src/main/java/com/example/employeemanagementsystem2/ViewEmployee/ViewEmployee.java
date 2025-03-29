package com.example.employeemanagementsystem2.ViewEmployee;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagementsystem2.MarkAttendance.Employee;
import com.example.employeemanagementsystem2.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ViewEmployee extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ViewEmployeeAdapter adapter;
    private List<Employee> employeeList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);

        recyclerView = findViewById(R.id.recyclerViewEmployees);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        employeeList = new ArrayList<>();
        adapter = new ViewEmployeeAdapter(employeeList);
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
