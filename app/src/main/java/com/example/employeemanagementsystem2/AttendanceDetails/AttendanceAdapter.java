package com.example.employeemanagementsystem2.AttendanceDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagementsystem2.MarkAttendance.AttendanceClass;
import com.example.employeemanagementsystem2.R;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private Context context;
    private List<AttendanceClass> attendanceList;

    public AttendanceAdapter(Context context, List<AttendanceClass> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceClass attendance = attendanceList.get(position);
        holder.dateTextView.setText(attendance.getDate());

        if (attendance.isPresent()) {
            holder.dateTextView.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.status.setText("Present");
            holder.status.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.dateTextView.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.status.setText("Absent");
            holder.status.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView,status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.textViewDate);
            status=itemView.findViewById(R.id.textViewStatus);
        }
    }
}
