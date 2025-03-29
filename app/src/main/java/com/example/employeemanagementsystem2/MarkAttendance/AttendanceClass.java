package com.example.employeemanagementsystem2.MarkAttendance;

public class AttendanceClass {
    private String date;
    private boolean isPresent;

    public AttendanceClass(String date, boolean isPresent) {
        this.date = date;
        this.isPresent = isPresent;
    }

    public String getDate() {
        return date;
    }

    public boolean isPresent() {
        return isPresent;
    }
}

