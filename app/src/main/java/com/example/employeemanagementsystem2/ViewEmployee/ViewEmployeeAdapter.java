package com.example.employeemanagementsystem2.ViewEmployee;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagementsystem2.MarkAttendance.Employee;
import com.example.employeemanagementsystem2.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewEmployeeAdapter extends RecyclerView.Adapter<ViewEmployeeAdapter.EmployeeViewHolder> {
    private List<Employee> employeeList;
    int count=0;

    public ViewEmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.txtName.setText(employee.getName());
        holder.txtEmail.setText(employee.getEmail());
        fetchCurrentMonthAttendance(employee.getUid(),holder.txtAttendance);
    }

    private void fetchCurrentMonthAttendance(String Uid,TextView txtAttendance) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        count=0;
        CollectionReference recordRef = db.collection("Attendance").document(Uid).collection("Record");
        String currentYearMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date()); // Example: "2025-03"

        recordRef.get()
                .addOnSuccessListener(querySnapshot -> {

                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String documentName = document.getId(); // Example: "2025-03-29"
                        if (documentName.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            String yearMonth = documentName.substring(0, 7); // Extract "yyyy-MM"
                            if (yearMonth.equals(currentYearMonth)) {
                                count++;
                            }
                        }
                    }
                    Log.d("Firestore", "Current Month Attendance Count: " + count);
                    txtAttendance.setText("Attendance for "+currentYearMonth+" : "+String.valueOf(count));
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching attendance records", e);
                });
    }
    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtAttendance;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtAttendance = itemView.findViewById(R.id.txtCount);
        }
    }
}
