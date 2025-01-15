//package com.example.bus_booking_system.api;
//
//import com.example.bus_booking_system.data.model.Bus;
//import com.example.bus_booking_system.data.model.Booking;
//import com.example.bus_booking_system.data.model.User;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.http.Body;
//import retrofit2.http.GET;
//import retrofit2.http.POST;
//import retrofit2.http.PUT;
//import retrofit2.http.Path;
//import retrofit2.http.Query;
//
//public interface ApiService {
//    // User endpoints
//    @POST("users/register")
//    Call<User> registerUser(@Body User user);
//
//    @POST("users/login")
//    Call<User> loginUser(@Query("email") String email, @Query("password") String password);
//
//    // Bus endpoints
//    @GET("buses")
//    Call<List<Bus>> getAllBuses();
//
//    @GET("buses/search")
//    Call<List<Bus>> searchBuses(@Query("source") String source, @Query("destination") String destination);
//
//    // Booking endpoints
//    @POST("bookings")
//    Call<Booking> createBooking(@Body Booking booking);
//
//    @GET("bookings/user/{userId}")
//    Call<List<Booking>> getUserBookings(@Path("userId") int userId);
//
//    @PUT("bookings/{bookingId}")
//    Call<Booking> updateBookingStatus(@Path("bookingId") int bookingId, @Query("status") String status);
//
//    @PUT("bookings/{bookingId}/payment")
//    Call<Booking> updatePaymentStatus(@Path("bookingId") int bookingId, @Query("status") String status);
//}