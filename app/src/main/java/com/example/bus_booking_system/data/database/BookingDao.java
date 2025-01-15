package com.example.bus_booking_system.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bus_booking_system.data.model.Booking;

import java.util.List;

@Dao
public interface BookingDao {
    @Insert
    long insert(Booking booking);
    
    @Update
    void update(Booking booking);
    
    @Delete
    void delete(Booking booking);
    
    @Query("SELECT * FROM bookings WHERE id = :id LIMIT 1")
    LiveData<Booking> getBookingById(int id);
    
    @Query("SELECT * FROM bookings WHERE userId = :userId")
    LiveData<List<Booking>> getUserBookings(int userId);
    
    @Query("SELECT * FROM bookings WHERE busId = :busId")
    LiveData<List<Booking>> getBusBookings(int busId);
    
    @Query("SELECT * FROM bookings WHERE busId = :busId AND journeyDate = :journeyDate")
    LiveData<List<Booking>> getBookingsByBusAndDate(int busId, String journeyDate);
    
    @Query("UPDATE bookings SET status = :status WHERE id = :bookingId")
    void updateBookingStatus(int bookingId, String status);
    
    @Query("UPDATE bookings SET paymentStatus = :paymentStatus WHERE id = :bookingId")
    void updatePaymentStatus(int bookingId, String paymentStatus);
} 