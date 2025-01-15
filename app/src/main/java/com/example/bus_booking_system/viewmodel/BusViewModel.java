package com.example.bus_booking_system.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.data.repository.BusRepository;
import com.example.bus_booking_system.data.model.Booking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BusViewModel extends AndroidViewModel {
    private BusRepository repository;
    private LiveData<List<Bus>> allBuses;

    public BusViewModel(Application application) {
        super(application);
        repository = new BusRepository(application);
        allBuses = repository.getAllBuses();
    }

    public LiveData<List<Bus>> getAllBuses() {
        return allBuses;
    }

    public LiveData<Bus> getBusById(int id) {
        return repository.getBusById(id);
    }

    public LiveData<List<Bus>> searchBuses(String source, String destination) {
        return repository.searchBuses(source, destination);
    }

    public void insert(Bus bus) {
        repository.insert(bus);
    }

    public void update(Bus bus) {
        repository.update(bus);
    }

    public void delete(Bus bus) {
        repository.delete(bus);
    }

    public void decreaseAvailableSeats(int busId) {
        repository.decreaseAvailableSeats(busId);
    }

    public void increaseAvailableSeats(int busId) {
        repository.increaseAvailableSeats(busId);
    }

    public LiveData<Integer> getAvailableSeats(int busId) {
        return repository.getAvailableSeats(busId);
    }

    public LiveData<boolean[]> getSeatStatus(int busId) {
        return repository.getSeatStatus(busId);
    }

    public void bookSeat(int busId, int seatNumber) {
        repository.bookSeat(busId, seatNumber);
    }

    public void releaseSeat(int busId, int seatNumber) {
        repository.releaseSeat(busId, seatNumber);
    }

    public LiveData<List<String>> getAllSources() {
        return repository.getAllSources();
    }

    public LiveData<List<String>> getAllDestinations() {
        return repository.getAllDestinations();
    }

    public LiveData<List<String>> getDestinationsForSource(String source) {
        return repository.getDestinationsForSource(source);
    }
    
    public LiveData<List<String>> getSourcesForDestination(String destination) {
        return repository.getSourcesForDestination(destination);
    }

    /**
     * Get the seat availability status for a specific bus on a journey date
     * @param busId The ID of the bus to check
     * @param journeyDate The date of travel in dd/MM/yyyy format
     * @return LiveData containing boolean array where true means seat is available
     */
    public LiveData<boolean[]> getBookedSeatsForDate(int busId, String journeyDate) {
        // First get the bus's total seat status
        LiveData<boolean[]> seatStatus = repository.getSeatStatus(busId);
//        System.out.println("LiveData<boolean[]> seatStatus = repository.getSeatStatus(busId)");
//        System.out.println(Arrays.toString(seatStatus.getValue()));
        // Then get bookings for this date to mark booked seats
        LiveData<List<Booking>> bookings = repository.getBookingsByBusAndDate(busId, journeyDate);
        // Combine the two LiveData sources to create the final
        // seat status
        return Transformations.switchMap(seatStatus, status -> Transformations.map(bookings, dateBookings -> {
            if (status == null) return null;
            // Create a copy of the original seat status
            boolean[] finalStatus = new boolean[30];
            for (int i = 0; i < 30; i++) {
                finalStatus[i] = true;
            }

            System.out.println("boolean[] finalStatus = status.clone()");

            System.out.println(Arrays.toString(finalStatus));
            System.out.println(Arrays.toString(dateBookings.toArray()));
            // Mark seats as unavailable based on bookings
            int i = 1;
            if (dateBookings != null) {
                for (Booking booking : dateBookings) {
                    System.out.println(i);
                    int seatNumber = booking.getSeatNumber();
                    if (seatNumber > 0 && seatNumber <= finalStatus.length) {
                        finalStatus[seatNumber - 1] = false; // Mark as booked
                    }
                    i++;
                }
            }
            System.out.println(Arrays.toString(finalStatus));
            return finalStatus;
        })
        );

    }
} 