package com.example.bus_booking_system.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "bookings",
        indices = {
            @Index("userId"),
            @Index("busId")
        },
        foreignKeys = {
            @ForeignKey(entity = User.class,
                    parentColumns = "id",
                    childColumns = "userId"),
            @ForeignKey(entity = Bus.class,
                    parentColumns = "id",
                    childColumns = "busId")
        })
public class Booking {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private int busId;
    private String bookingDate;
    private String journeyDate;
    private int seatNumber;
    private double totalFare;
    private String status; // CONFIRMED, CANCELLED, PENDING
    private String paymentStatus; // PAID, PENDING
    private String seatStatus; // AVAILABLE, BOOKED, RESERVED, BLOCKED
    
    // Primary constructor with seat management
    public Booking(int userId, int busId, String bookingDate, String journeyDate,
                  int seatNumber, double totalFare) {
        this.userId = userId;
        this.busId = busId;
        this.bookingDate = bookingDate;
        this.journeyDate = journeyDate;
        this.seatNumber = seatNumber;
        this.totalFare = totalFare;
        this.status = "CONFIRMED";
        this.paymentStatus = "PENDING";
        this.seatStatus = "RESERVED";
    }

    @Ignore
    // Full constructor with all parameters
    public Booking(int userId, int busId, String bookingDate, String journeyDate,
                  int seatNumber, double totalFare, String status, 
                  String paymentStatus, String seatStatus) {
        this.userId = userId;
        this.busId = busId;
        this.bookingDate = bookingDate;
        this.journeyDate = journeyDate;
        this.seatNumber = seatNumber;
        this.totalFare = totalFare;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.seatStatus = seatStatus;
    }

    // Helper method to check if seat is available
    public boolean isSeatAvailable() {
        return "AVAILABLE".equals(this.seatStatus);
    }

    // Helper method to check if seat can be booked
    public boolean canBookSeat() {
        return "AVAILABLE".equals(this.seatStatus) || 
               "RESERVED".equals(this.seatStatus);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getBusId() { return busId; }
    public void setBusId(int busId) { this.busId = busId; }
    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    public String getJourneyDate() { return journeyDate; }
    public void setJourneyDate(String journeyDate) { this.journeyDate = journeyDate; }
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
    public double getTotalFare() { return totalFare; }
    public void setTotalFare(double totalFare) { this.totalFare = totalFare; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getSeatStatus() { return seatStatus; }
    public void setSeatStatus(String seatStatus) { this.seatStatus = seatStatus; }
} 