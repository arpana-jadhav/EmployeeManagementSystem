package com.example.employeemanagementsystem2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HomeFragment extends Fragment {

    private Button btnCheckIn, btnCheckOut;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String usersId,email,name;
    FirebaseUser user;
    private String checkInTime;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCheckIn = view.findViewById(R.id.checkInButton);
        btnCheckOut = view.findViewById(R.id.checkOutButton);

        db = FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        usersId = user.getUid();
        email=user.getEmail();
        fetchName(usersId);

        btnCheckIn.setOnClickListener(v -> markCheckIn());
        btnCheckOut.setOnClickListener(v -> markCheckOut());

        checkPreviousAttendance(); // Check if the user already checked in
    }
    private void markCheckIn() {
        checkInTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        Map<String, Object> attendance = new HashMap<>();
        attendance.put("checkInTime", checkInTime);
        attendance.put("date", date);
        attendance.put("status", "Checked-In");
        attendance.put("emailId",email);
        attendance.put("Employee Name",name);

        db.collection("Attendance").document(usersId)
                .collection("Record")
                .document(date)
                .set(attendance)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Checked-In Successfully!", Toast.LENGTH_SHORT).show();
                    btnCheckIn.setVisibility(View.VISIBLE);
                    btnCheckOut.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    private void markCheckOut() {
        Log.d("checkout","Checkout Function");
        String checkOutTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        db.collection("Attendance").document(usersId)
                .collection("Record")
                .document(date)
                .get().addOnSuccessListener(documentSnapshot -> {
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

                            db.collection("Attendance").document(usersId).collection("Record").document(date)
                                    .update(updateAttendance)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getActivity(), "Checked-Out Successfully!", Toast.LENGTH_SHORT).show();
                                        btnCheckOut.setVisibility(View.VISIBLE);
                                        btnCheckIn.setVisibility(View.VISIBLE);
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void checkPreviousAttendance() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        db.collection("Attendance").document(usersId).collection("records").document(date)
                .get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("checkInTime")) {
                        btnCheckIn.setVisibility(View.VISIBLE);
                        btnCheckOut.setVisibility(View.VISIBLE);
                    }
                });
    }
    public void fetchName(String uid){
        DocumentReference userRef = db.collection("users").document(uid);

        userRef.get().addOnSuccessListener(document -> {
            if (document.exists()) {
                name = document.getString("Employee Name");
                String email = document.getString("Email");
            } else {
                Toast.makeText(requireContext(), "User not found!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Error fetching user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
