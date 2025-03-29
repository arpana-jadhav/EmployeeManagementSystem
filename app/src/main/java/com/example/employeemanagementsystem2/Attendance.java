package com.example.employeemanagementsystem2;


public class Attendance {
    private String userId;
    private String date;
    private String status;
    private String timeIn;
    private String timeOut;

    public Attendance() {}

    public Attendance(String userId, String date, String status, String timeIn, String timeOut) {
        this.userId = userId;
        this.date = date;
        this.status = status;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public String getUserId() { return userId; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getTimeIn() { return timeIn; }
    public String getTimeOut() { return timeOut; }
}
