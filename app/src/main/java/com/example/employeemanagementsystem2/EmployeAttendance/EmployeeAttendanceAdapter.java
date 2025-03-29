package com.example.employeemanagementsystem2.EmployeAttendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagementsystem2.R;

import java.util.List;

public class EmployeeAttendanceAdapter extends RecyclerView.Adapter<EmployeeAttendanceAdapter.ViewHolder> {
    private List<Attendance> attendanceList;

    public EmployeeAttendanceAdapter(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);
        holder.txtEmployeeName.setText("User: " + attendance.getEmployeeName());
        holder.txtEmailId.setText("Email: " + attendance.getEmailId());
        holder.txtDate.setText("Date: " + attendance.getDate());
        holder.txtStatus.setText("Status: " + attendance.getStatus());
        holder.txtTimeIn.setText("Time In: " + attendance.getTimeIn());
        holder.txtTimeOut.setText("Time Out: " + attendance.getTimeOut());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEmployeeName, txtEmailId, txtDate, txtStatus, txtTimeIn, txtTimeOut;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEmployeeName = itemView.findViewById(R.id.txtUserName);
            txtEmailId = itemView.findViewById(R.id.txtEmailId);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtTimeIn = itemView.findViewById(R.id.txtTimeIn);
            txtTimeOut = itemView.findViewById(R.id.txtTimeOut);
        }
    }
}
