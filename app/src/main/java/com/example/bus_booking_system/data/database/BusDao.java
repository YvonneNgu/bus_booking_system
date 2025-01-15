package com.example.bus_booking_system.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bus_booking_system.data.model.Bus;

import java.util.List;

@Dao
public interface BusDao {
    @Insert
    long insert(Bus bus);
    
    @Update
    void update(Bus bus);
    
    @Delete
    void delete(Bus bus);
    
    @Query("SELECT * FROM buses WHERE id = :id LIMIT 1")
    LiveData<Bus> getBusById(int id);
    
    @Query("SELECT * FROM buses")
    LiveData<List<Bus>> getAllBuses();
    
    @Query("SELECT * FROM buses WHERE source = :source AND destination = :destination")
    LiveData<List<Bus>> searchBuses(String source, String destination);
    
    @Query("UPDATE buses SET availableSeats = availableSeats - 1 WHERE id = :busId AND availableSeats > 0")
    void decreaseAvailableSeats(int busId);
    
    @Query("UPDATE buses SET availableSeats = availableSeats + 1 WHERE id = :busId AND availableSeats < totalSeats")
    void increaseAvailableSeats(int busId);
} 