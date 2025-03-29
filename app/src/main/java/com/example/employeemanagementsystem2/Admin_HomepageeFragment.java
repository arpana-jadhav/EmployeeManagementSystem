package com.example.employeemanagementsystem2;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Admin_HomepageeFragment extends Fragment {
    TextView ViewAttendance;
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
        ViewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ViewAttendanceActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}