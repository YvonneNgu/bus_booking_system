package com.example.bus_booking_system.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "buses")
@TypeConverters(BooleanArrayConverter.class)
public class Bus {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String busName;
    private String busNumber;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private double fare;
    private int totalSeats;
    private int availableSeats;
    private boolean[] seatStatus; // true for available, false for booked

    public Bus(String busName, String busNumber, String source, String destination,
              String departureTime, String arrivalTime, double fare, int totalSeats, int availableSeats) {
        this.busName = busName;
        this.busNumber = busNumber;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.fare = fare;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.seatStatus = new boolean[totalSeats];
        for (int i = 0; i < totalSeats; i++) {
            seatStatus[i] = true;
        }
    }

    // Enhanced seat management methods
    public boolean isSeatAvailable(int seatNumber) {
        return seatNumber > 0 && seatNumber <= totalSeats && seatStatus[seatNumber - 1];
    }

    public boolean bookSeat(int seatNumber) {
        if (isSeatAvailable(seatNumber)) {
            seatStatus[seatNumber - 1] = false;
            availableSeats--;
            return true;
        }
        return false;
    }

    public boolean cancelSeat(int seatNumber) {
        if (seatNumber > 0 && seatNumber <= totalSeats && !seatStatus[seatNumber - 1]) {
            seatStatus[seatNumber - 1] = true;
            availableSeats++;
            return true;
        }
        return false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBusName() { return busName; }
    public void setBusName(String busName) { this.busName = busName; }
    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public boolean[] getSeatStatus() { return seatStatus; }
    public void setSeatStatus(boolean[] seatStatus) { this.seatStatus = seatStatus; }
}

