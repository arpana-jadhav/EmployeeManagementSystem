package com.example.employeemanagementsystem2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment {

    CardView logout;
    CardView attendance,salary;

    FirebaseUser user;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        user=FirebaseAuth.getInstance().getCurrentUser();

        logout=view.findViewById(R.id.logout);
        salary=view.findViewById(R.id.Salary);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("error","Button Clicked");
              alertDialog();
            }
        });
        attendance=view.findViewById(R.id.Attendance);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(requireActivity(),AttendanceActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCurrentMonthAttendance();
            }
        });

        return view;

    }

    public void alertDialog(){
        AlertDialog dialog=new AlertDialog.Builder(getActivity())
                .setTitle("Confirm Logout")
                .setMessage("Are your sure you want to logout")
                .setPositiveButton("yes",(dialog1, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(requireActivity(), Login.class);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton("No",(dialog1,which)->{
                    dialog1.dismiss();
                })
                .create();
        dialog.show();
        dialog.setCancelable(false);
    }

    private void fetchUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String uid = user.getUid();

            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> userData = documentSnapshot.getData();
                            if (userData != null) {
                                Log.d("Firestore", "User Data: " + userData.toString());
                                List<Map<String, Object>> userList = new ArrayList<>();
                                userList.add(userData); // Convert to list for report generation
                               // pdfExporter.generateUserDetails(requireContext(), userList);
                            }
                        } else {
                            Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error fetching user data", e);
                    });
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }


    }

    private void fetchCurrentMonthAttendance() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String uid = user.getUid();
            CollectionReference attendanceRef = db.collection("Attendance").document(uid).getParent();

            attendanceRef.get().addOnSuccessListener(querySnapshot -> {
                if (!querySnapshot.isEmpty()) {
                    int count = getCurrentMonthAttendanceCount(querySnapshot);
                    Log.d("Firestore", "Current Month Attendance Count: " + count);
                    Toast.makeText(requireContext(), "Attendance: " + count, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Firestore", "No attendance records found for the user.");
                    Toast.makeText(requireContext(), "No attendance records found.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Error fetching attendance records", e);
                Toast.makeText(requireContext(), "Error fetching attendance", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCurrentMonthAttendanceCount(QuerySnapshot querySnapshot) {
        Calendar currentCalendar = Calendar.getInstance();
        int currentMonth = currentCalendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        int currentYear = currentCalendar.get(Calendar.YEAR);
        int count = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (QueryDocumentSnapshot document : querySnapshot) {
            String dateStr = document.getId(); // Collection ID is the date (yyyy-MM-dd)
            try {
                Date checkInDate = sdf.parse(dateStr);
                Calendar checkInCalendar = Calendar.getInstance();
                checkInCalendar.setTime(checkInDate);

                int checkInMonth = checkInCalendar.get(Calendar.MONTH) + 1; // Adjusting zero-based index
                int checkInYear = checkInCalendar.get(Calendar.YEAR);

                if (checkInMonth == currentMonth && checkInYear == currentYear) {
                    count++;
                }
            } catch (ParseException e) {
                Log.e("Date Parsing", "Error parsing date: " + dateStr, e);
            }
        }
        return count;
    }
}
