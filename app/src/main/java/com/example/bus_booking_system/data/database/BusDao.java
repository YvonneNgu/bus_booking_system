package com.example.bus_booking_system.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.data.model.Booking;

import java.util.List;

@Dao
public interface BusDao {
    @Insert
    long insert(Bus bus);
    
    @Update
    void update(Bus bus);
    
    @Delete
    void delete(Bus bus);

    @Query("SELECT * FROM buses")
    LiveData<List<Bus>> getAllBuses();
    
    @Query("SELECT * FROM buses WHERE id = :id LIMIT 1")
    LiveData<Bus> getBusById(int id);
    
    @Query("SELECT * FROM buses WHERE source = :source AND destination = :destination")
    LiveData<List<Bus>> searchBuses(String source, String destination);
    
    @Query("SELECT DISTINCT source FROM buses")
    LiveData<List<String>> getAllSources();

    @Query("SELECT DISTINCT destination FROM buses")
    LiveData<List<String>> getAllDestinations();
    
    @Query("SELECT DISTINCT destination FROM buses WHERE source = :source")
    LiveData<List<String>> getDestinationsForSource(String source);
    
    @Query("SELECT DISTINCT source FROM buses WHERE destination = :destination")
    LiveData<List<String>> getSourcesForDestination(String destination);
    
    @Query("UPDATE buses SET availableSeats = availableSeats - 1 WHERE id = :busId")
    void decrementAvailableSeats(int busId);
    
    @Query("UPDATE buses SET availableSeats = availableSeats + 1 WHERE id = :busId")
    void incrementAvailableSeats(int busId);

    @Query("SELECT availableSeats FROM buses WHERE id = :busId")
    LiveData<Integer> getAvailableSeats(int busId);

    @Query("SELECT seatStatus FROM buses WHERE id = :busId")
    LiveData<boolean[]> getSeatStatus(int busId);

    @Query("UPDATE buses SET seatStatus = :seatStatus WHERE id = :busId")
    void updateSeatStatus(int busId, boolean[] seatStatus);

    @Insert
    long insertSync(Bus bus);

    @Query("SELECT * FROM bookings WHERE busId = :busId AND journeyDate = :journeyDate")
    List<Booking> getBookingsByBusAndDateSync(int busId, String journeyDate);
} 