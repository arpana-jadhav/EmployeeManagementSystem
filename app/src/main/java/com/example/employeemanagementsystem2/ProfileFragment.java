package com.example.employeemanagementsystem2;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    CardView logout;
    FirebaseUser user;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        user=FirebaseAuth.getInstance().getCurrentUser();

        logout=view.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("error","Button Clicked");
              alertDialog();
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
}