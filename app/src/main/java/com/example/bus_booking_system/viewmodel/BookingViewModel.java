package com.example.bus_booking_system.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

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
            System.out.println("insertSync");
            // Attempt to insert booking synchronously
            boolean result = repository.insertSync(booking, new BookingRepository.BookingCallback() {
                @Override
                public void onSuccess(long bookingId) {
                    Toast.makeText(BookingViewModel.this.getApplication(), "Booking inserted successfully with ID: " + bookingId, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(BookingViewModel.this.getApplication(), "Booking insertion failed: " + error, Toast.LENGTH_SHORT).show();
                }
            });
            System.out.println("boolean result = repository.insertSync(booking);");
            System.out.println("result");

            Log.d("BookingViewModel", "Synchronous booking insert result: " + result);
            return result;
        } catch (Exception e) {
            Log.e("BookingViewModel", "Error in synchronous booking insert: " + e.getMessage());
            return false;
        }
    }
} 