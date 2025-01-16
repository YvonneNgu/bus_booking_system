package com.example.bus_booking_system.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.bus_booking_system.data.database.AppDatabase;
import com.example.bus_booking_system.data.database.BookingDao;
import com.example.bus_booking_system.data.model.Booking;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;

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

//    public void insert(Booking booking, BookingCallback callback) {
//        executorService.execute(() -> {
//            try {
//                // Check if user and bus exist
//                if (!bookingDao.checkUserExists(booking.getUserId())) {
//                    Log.e("BookingRepository", "User not found: " + booking.getUserId());
//                    callback.onError("User not found");
//                    return;
//                }
//                if (!bookingDao.checkBusExists(booking.getBusId())) {
//                    Log.e("BookingRepository", "Bus not found: " + booking.getBusId());
//                    callback.onError("Bus not found");
//                    return;
//                }
//
//                // Insert booking into the database
//                long id = bookingDao.insert(booking);
//                Log.d("BookingRepository", "Booking inserted with ID: " + id);
//                callback.onSuccess(id);
//            } catch (Exception e) {
//                Log.e("BookingRepository", "Error inserting booking: " + e.getMessage());
//                callback.onError(e.getMessage());
//            }
//        });
//
//        public boolean insert(Booking booking, BookingCallback callback) {
//            final boolean[] result = {false};
//            executorService.execute(() -> {
//                try {
//                    // Check if user and bus exist
//                    if (!bookingDao.checkUserExists(booking.getUserId())) {
//                        Log.e("BookingRepository", "User not found: " + booking.getUserId());
//                        callback.onError("User not found");
//                        return ;
//                    }
//                    if (!bookingDao.checkBusExists(booking.getBusId())) {
//                        Log.e("BookingRepository", "Bus not found: " + booking.getBusId());
//                        callback.onError("Bus not found");
//                        return ;
//                    }
//
//                    result[0] = bookingDao.insert(booking) > 0;
//                    return result[0];
//
//                } catch (Exception e) {
//                    Log.e("BookingRepository", "Error inserting booking: " + e.getMessage());
//                    callback.onError(e.getMessage());
//                }
//                return result[0];
//            });
//
//
//
//
//            executorService.execute(() -> {
//            try {
//                Log.d("insertSync", String.valueOf((bookingDao.insertSync(booking))));
//                // Insert booking synchronously
//                result[0] = bookingDao.insert(booking) > 0;
//                Log.d("BookingRepository", "Synchronous booking insert result: " + result[0]);
//            } catch (Exception e) {
//                Log.e("BookingRepository", "Error in synchronous booking insert: " + e.getMessage());
//            }
//        });
//        return result[0];
//    }

    public boolean insertSync(Booking booking, BookingCallback callback) {
        final boolean[] result = {false};
        CountDownLatch latch = new CountDownLatch(1);
        executorService.execute(() -> {
            try {
                // Insert booking synchronously
                result[0] = bookingDao.insert(booking) > 0;
                Log.d("BookingRepository", "Synchronous booking insert result: " + result[0]);
            } catch (Exception e) {
                Log.e("BookingRepository", "Error in synchronous booking insert: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });
        try {
            latch.await(); // Wait for the task to complete
        } catch (InterruptedException e) {
            Log.e("BookingRepository", "Interrupted while waiting for booking insert: " + e.getMessage());
        }
        return result[0];
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

    public interface BookingCallback {
        void onSuccess(long bookingId);
        void onError(String error);
    }
} 