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
    
    /**
     * Get bookings for a specific bus on a specific date
     * Used to check seat availability for a particular journey date
     * @param busId The ID of the bus
     * @param journeyDate The date of travel in dd/MM/yyyy format
     * @return List of bookings for that bus and date
     */
    @Query("SELECT * FROM bookings WHERE busId = :busId AND journeyDate = :journeyDate")
    LiveData<List<Booking>> getBookingsByBusAndDate(int busId, String journeyDate);
    
    @Query("UPDATE bookings SET status = :status WHERE id = :bookingId")
    void updateBookingStatus(int bookingId, String status);
    
    @Query("UPDATE bookings SET paymentStatus = :paymentStatus WHERE id = :bookingId")
    void updatePaymentStatus(int bookingId, String paymentStatus);
    
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE id = :userId LIMIT 1)")
    boolean checkUserExists(int userId);
    
    @Query("SELECT EXISTS(SELECT 1 FROM buses WHERE id = :busId LIMIT 1)")
    boolean checkBusExists(int busId);
    
    /**
     * Check if a specific seat is booked for a journey date
     * @param busId The ID of the bus
     * @param journeyDate The date of travel in dd/MM/yyyy format
     * @param seatNumber The seat number to check
     * @return Number of bookings for that seat (0 if available, 1 if booked)
     */
    @Query("SELECT COUNT(*) FROM bookings WHERE busId = :busId AND journeyDate = :journeyDate AND seatNumber = :seatNumber")
    LiveData<Integer> isSeatBooked(int busId, String journeyDate, String seatNumber);
    
    @Query("SELECT * FROM bookings WHERE busId = :busId AND journeyDate = :journeyDate")
    LiveData<List<Booking>> getBookedSeats(int busId, String journeyDate);
    
    @Query("SELECT * FROM bookings WHERE busId = :busId AND journeyDate = :journeyDate AND seatNumber = :seatNumber LIMIT 1")
    LiveData<Booking> getBookingBySeat(int busId, String journeyDate, String seatNumber);
} 