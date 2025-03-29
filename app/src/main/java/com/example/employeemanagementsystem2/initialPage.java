package com.example.employeemanagementsystem2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class initialPage extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        user = auth.getCurrentUser();

        if (user != null) {
            checkUserRole(user.getUid());
        } else {
            startActivity(new Intent(initialPage.this, Login.class));
            finish();
        }
    }

    private void checkUserRole(String userId) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        DocumentSnapshot document = task.getResult();
                        String role = document.getString("Role");

                        if ("Admin".equals(role)) {
                            startActivity(new Intent(initialPage.this, admin_code.class));
                        } else {
                            startActivity(new Intent(initialPage.this, MainActivity.class));
                        }
                    } else {
                        Toast.makeText(initialPage.this, "Session Time Out, Login Again", Toast.LENGTH_SHORT).show();
                        Log.e("FirestoreDebug", "Error fetching role", task.getException());
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(initialPage.this, Login.class);
                        startActivity(intent);
                    }
                    finish();
                });
    }
}
