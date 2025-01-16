package com.example.bus_booking_system.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bus_booking_system.data.model.Booking;
import com.example.bus_booking_system.data.repository.BookingRepository;

import java.util.List;

public class BookingViewModel extends AndroidViewModel {
    private BookingRepository repository;

    public BookingViewModel(Application application) {
        super(application);
        repository = new BookingRepository(application);
    }

    public LiveData<Booking> getBookingById(int id) {
        return repository.getBookingById(id);
    }

    public LiveData<List<Booking>> getUserBookings(int userId) {
        return repository.getUserBookings(userId);
    }

    public LiveData<List<Booking>> getBusBookings(int busId) {
        return repository.getBusBookings(busId);
    }

    public LiveData<List<Booking>> getBookingsByBusAndDate(int busId, String journeyDate) {
        return repository.getBookingsByBusAndDate(busId, journeyDate);
    }

    public void insert(Booking booking, BookingRepository.BookingCallback callback) {
        repository.insert(booking, callback);
    }

    public void update(Booking booking) {
        repository.update(booking);
    }

    public void delete(Booking booking) {
        repository.delete(booking);
    }

    public void updateBookingStatus(int bookingId, String status) {
        repository.updateBookingStatus(bookingId, status);
    }

    public void updatePaymentStatus(int bookingId, String paymentStatus) {
        repository.updatePaymentStatus(bookingId, paymentStatus);
    }

    public boolean insertSync(Booking booking) {
        try {
            // Attempt to insert booking synchronously
            boolean result = repository.insertSync(booking);
            Log.d("BookingViewModel", "Synchronous booking insert result: " + result);
            return result;
        } catch (Exception e) {
            Log.e("BookingViewModel", "Error in synchronous booking insert: " + e.getMessage());
            return false;
        }
    }
} 