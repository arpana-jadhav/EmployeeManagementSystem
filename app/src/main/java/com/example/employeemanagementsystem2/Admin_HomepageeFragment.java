package com.example.employeemanagementsystem2;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.employeemanagementsystem2.EmployeAttendance.ViewAttendanceActivity;
import com.example.employeemanagementsystem2.MarkAttendance.MarkAttendance;
import com.example.employeemanagementsystem2.ViewEmployee.ViewEmployee;

public class Admin_HomepageeFragment extends Fragment {
    TextView ViewAttendance,markAttendance,viewEmployee;
    public Admin_HomepageeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_admin__homepagee,container,false);

        ViewAttendance=view.findViewById(R.id.view_attendance);
        markAttendance=view.findViewById(R.id.mark_attendance);
        viewEmployee=view.findViewById(R.id.view_employee);


        ViewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ViewAttendanceActivity.class);
                startActivity(intent);
            }
        });

        markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), MarkAttendance.class);
                startActivity(intent);
            }
        });

        viewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ViewEmployee.class);
                startActivity(intent);
            }
        });


        return view;
    }
}