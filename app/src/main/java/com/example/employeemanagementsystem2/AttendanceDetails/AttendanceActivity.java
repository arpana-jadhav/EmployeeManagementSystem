package com.example.employeemanagementsystem2.AttendanceDetails;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagementsystem2.MarkAttendance.AttendanceClass;
import com.example.employeemanagementsystem2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private List<AttendanceClass> attendanceList;
    private FirebaseFirestore db;
    FirebaseUser user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        user= FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        attendanceList = new ArrayList<>();
        adapter = new AttendanceAdapter(this, attendanceList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        fetchAttendanceRecords();
    }

    private void fetchAttendanceRecords() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        List<String> allDates = new ArrayList<>();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= daysInMonth; i++) {
            String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", currentYear, currentMonth, i);
            allDates.add(date);
        }

        CollectionReference attendanceRef = db.collection("Attendance")
                .document(userId)
                .collection("Record");

        attendanceRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<String> presentDates = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    presentDates.add(document.getId());
                }

                for (String date : allDates) {
                    boolean isPresent = presentDates.contains(date);
                    attendanceList.add(new AttendanceClass(date, isPresent));
                }

                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Error fetching attendance", Toast.LENGTH_SHORT).show()
        );
    }
}
