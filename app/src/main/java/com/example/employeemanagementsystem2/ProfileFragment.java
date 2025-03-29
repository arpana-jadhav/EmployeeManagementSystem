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
import android.widget.Toast;

import com.example.employeemanagementsystem2.AttendanceDetails.AttendanceActivity;
import com.example.employeemanagementsystem2.SalaryReport.report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    CardView logout;
    CardView attendance,salary,personalDetails;
    int count=0;

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

        fetchCurrentMonthAttendance();
        logout=view.findViewById(R.id.logout);
        salary=view.findViewById(R.id.Salary);
        personalDetails=view.findViewById(R.id.PersonalDetails);

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
                Intent intent=new Intent(requireActivity(), AttendanceActivity.class);
                startActivity(intent);
            }
        });

        salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog=new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm Export")
                        .setMessage("Are your sure you want to export salary Report")
                        .setPositiveButton("yes",(dialog1, which) -> {
                            fetchUserDetailsAndExportPDF();
                        })
                        .setNegativeButton("No",(dialog1,which)->{
                            dialog1.dismiss();
                        })
                        .create();
                dialog.show();
                dialog.setCancelable(false);
            }
        });

        personalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(requireActivity(),PersonalDetails.class);
                startActivity(intent);
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

    private void fetchUserDetailsAndExportPDF() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String currentYearMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());

        if (user == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();
        DocumentReference userRef = db.collection("users").document(uid);

        userRef.get().addOnSuccessListener(document -> {
            if (document.exists()) {
                List<List<String>> userDataList = new ArrayList<>();

                // Table Header
                userDataList.add(Arrays.asList("Field", "Value"));

                String name = document.getString("Employee Name");
                String email = document.getString("Email");
                String phone = document.getString("Contact");
                String address = document.getString("Address");

                int salary = count * 450;

                if (name != null && email != null && phone != null && address != null) {
                    userDataList.add(Arrays.asList("Employee Name", name));
                    userDataList.add(Arrays.asList("Email", email));
                    userDataList.add(Arrays.asList("Phone", phone));
                    userDataList.add(Arrays.asList("Address", address));
                    userDataList.add(Arrays.asList("Attendance Count", String.valueOf(count)));
                    userDataList.add(Arrays.asList("Salary Till "+currentYearMonth, "₹" + salary));

                    Log.d("PDF", "User Data List: " + userDataList.toString());

                    // Generate the PDF
                    report.generatePDF(requireContext(), userDataList,count);
                } else {
                    Toast.makeText(requireContext(), "Incomplete user data!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "User not found!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Error fetching user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchCurrentMonthAttendance() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        count=0;
        String uid = user.getUid();
        CollectionReference recordRef = db.collection("Attendance").document(uid).collection("Record");

        recordRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    String currentYearMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date()); // Example: "2025-03"

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
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching attendance records", e);
                    Toast.makeText(requireContext(), "Error fetching attendance", Toast.LENGTH_SHORT).show();
                });
    }
}
