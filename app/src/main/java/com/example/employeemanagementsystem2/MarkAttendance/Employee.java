package com.example.employeemanagementsystem2.MarkAttendance;

public class Employee {
    private String name;
    private String email,uid;

    public Employee() {
        // Required empty constructor for Firestore
    }

    public Employee(String uid,String name, String email) {
        this.uid=uid;
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }
}

