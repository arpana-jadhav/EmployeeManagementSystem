package com.example.employeemanagementsystem2.MarkAttendance;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagementsystem2.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private List<Employee> employeeList;
    private FirebaseFirestore db;

    public EmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.txtName.setText(employee.getName());
        holder.txtEmail.setText(employee.getEmail());

        holder.btnCheckIn.setOnClickListener(v -> markCheckIn(employee.getUid(),employee.getEmail(),employee.getName()));
        holder.btnCheckOut.setOnClickListener(v -> markCheckOut(employee.getUid()));
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail;
        Button btnCheckIn, btnCheckOut;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtEmployeeName);
            txtEmail = itemView.findViewById(R.id.txtEmployeeEmail);
            btnCheckIn = itemView.findViewById(R.id.btnCheckIn);
            btnCheckOut = itemView.findViewById(R.id.btnCheckOut);
        }
    }

    private void markCheckIn(String uid,String email,String name) {
        String checkInTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Map<String, Object> attendance = new HashMap<>();
        attendance.put("checkInTime", checkInTime);
        attendance.put("date", date);
        attendance.put("status", "Checked-In");
        attendance.put("emailId",email);
        attendance.put("Employee Name",name);

        db.collection("Attendance").document(uid)
                .collection("Record").document(date)
                .set(attendance)
                .addOnSuccessListener(aVoid -> Log.d("Attendance", "Checked-In Successfully!"))
                .addOnFailureListener(e -> Log.e("Error", "Error: " + e.getMessage()));
    }

    private void markCheckOut(String uid) {
        String checkOutTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        db.collection("Attendance").document(uid)
                .collection("Record").document(date)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String checkInTime = documentSnapshot.getString("checkInTime");
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        try {
                            Date checkIn = format.parse(checkInTime);
                            Date checkOut = format.parse(checkOutTime);
                            long durationMillis = checkOut.getTime() - checkIn.getTime();
                            long hours = (durationMillis / (1000 * 60 * 60)) % 24;
                            long minutes = (durationMillis / (1000 * 60)) % 60;

                            Map<String, Object> updateAttendance = new HashMap<>();
                            updateAttendance.put("checkOutTime", checkOutTime);
                            updateAttendance.put("workDuration", hours + "h " + minutes + "m");
                            updateAttendance.put("status", "Checked-Out");

                            db.collection("Attendance").document(uid)
                                    .collection("Record").document(date)
                                    .update(updateAttendance)
                                    .addOnSuccessListener(aVoid -> Log.d("Attendance", "Checked-Out Successfully!"))
                                    .addOnFailureListener(e -> Log.e("Error", "Error: " + e.getMessage()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
