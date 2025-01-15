package com.example.bus_booking_system.viewmodel;

import android.app.Application;

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

    public void insert(Booking booking) {
        repository.insert(booking);
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
} 