package com.example.employeemanagementsystem2.EmployeAttendance;

public class Attendance {
    private String employeeName;
    private String emailId;
    private String date;
    private String status;
    private String timeIn;
    private String timeOut;

    public Attendance() {}

    public Attendance(String employeeName, String emailId, String date, String status, String timeIn, String timeOut) {
        this.employeeName = employeeName;
        this.emailId = emailId;
        this.date = date;
        this.status = status;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }
}
