package com.example.bus_booking_system.api;

import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.data.model.Booking;
import com.example.bus_booking_system.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // User endpoints
    // Used in UserRepository for registering a new user
    @POST("users/register")
    Call<User> registerUser(@Body User user);

    // Used in UserRepository for logging in a user
    @POST("users/login")
    Call<User> loginUser(@Query("email") String email, @Query("password") String password);

    // Bus endpoints
    // Used in BusRepository to fetch all buses
    @GET("buses")
    Call<List<Bus>> getAllBuses();

    // Used in BusRepository to search for buses by source and destination
    @GET("buses/search")
    Call<List<Bus>> searchBuses(@Query("source") String source, @Query("destination") String destination);

    // Booking endpoints
    // Used in BookingRepository to create a new booking
    @POST("bookings")
    Call<Booking> createBooking(@Body Booking booking);

    // Used in BookingRepository to fetch bookings for a specific user
    @GET("bookings/user/{userId}")
    Call<List<Booking>> getUserBookings(@Path("userId") int userId);

    // Used in BookingRepository to update the status of a booking
    @PUT("bookings/{bookingId}")
    Call<Booking> updateBookingStatus(@Path("bookingId") int bookingId, @Query("status") String status);

    // Used in BookingRepository to update the payment status of a booking
    @PUT("bookings/{bookingId}/payment")
    Call<Booking> updatePaymentStatus(@Path("bookingId") int bookingId, @Query("status") String status);
} 