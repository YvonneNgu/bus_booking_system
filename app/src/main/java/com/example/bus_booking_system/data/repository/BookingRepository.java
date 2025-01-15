package com.example.bus_booking_system.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bus_booking_system.data.database.AppDatabase;
import com.example.bus_booking_system.data.database.BookingDao;
import com.example.bus_booking_system.data.model.Booking;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookingRepository {
    private BookingDao bookingDao;
    private ExecutorService executorService;

    public BookingRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        bookingDao = db.bookingDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Booking> getBookingById(int id) {
        return bookingDao.getBookingById(id);
    }

    public LiveData<List<Booking>> getUserBookings(int userId) {
        return bookingDao.getUserBookings(userId);
    }

    public LiveData<List<Booking>> getBusBookings(int busId) {
        return bookingDao.getBusBookings(busId);
    }

    public LiveData<List<Booking>> getBookingsByBusAndDate(int busId, String journeyDate) {
        return bookingDao.getBookingsByBusAndDate(busId, journeyDate);
    }

    public void insert(Booking booking) {
        executorService.execute(() -> bookingDao.insert(booking));
    }

    public void update(Booking booking) {
        executorService.execute(() -> bookingDao.update(booking));
    }

    public void delete(Booking booking) {
        executorService.execute(() -> bookingDao.delete(booking));
    }

    public void updateBookingStatus(int bookingId, String status) {
        executorService.execute(() -> bookingDao.updateBookingStatus(bookingId, status));
    }

    public void updatePaymentStatus(int bookingId, String paymentStatus) {
        executorService.execute(() -> bookingDao.updatePaymentStatus(bookingId, paymentStatus));
    }
} 