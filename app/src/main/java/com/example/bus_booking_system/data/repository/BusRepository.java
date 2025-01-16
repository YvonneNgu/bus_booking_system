package com.example.bus_booking_system.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bus_booking_system.data.database.AppDatabase;
import com.example.bus_booking_system.data.database.BusDao;
import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.data.model.Booking;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BusRepository {
    private BusDao busDao;
    private LiveData<List<Bus>> allBuses;
    private ExecutorService executorService;
    private Application application;

    public BusRepository(Application application) {
        this.application = application;
        AppDatabase db = AppDatabase.getDatabase(application);
        busDao = db.busDao();
        allBuses = busDao.getAllBuses();
        executorService = Executors.newSingleThreadExecutor();
    }

    protected Application getApplication() {
        return application;
    }

    public LiveData<List<Bus>> getAllBuses() {
        return allBuses;
    }

    public LiveData<Bus> getBusById(int id) {
        return busDao.getBusById(id);
    }

    public LiveData<List<Bus>> searchBuses(String source, String destination) {
        return busDao.searchBuses(source, destination);
    }

    public void insert(Bus bus) {
        executorService.execute(() -> busDao.insert(bus));
    }

    public void update(Bus bus) {
        executorService.execute(() -> busDao.update(bus));
    }

    public void delete(Bus bus) {
        executorService.execute(() -> busDao.delete(bus));
    }

    public void decreaseAvailableSeats(int busId) {
        executorService.execute(() -> busDao.decrementAvailableSeats(busId));
    }

    public void increaseAvailableSeats(int busId) {
        executorService.execute(() -> busDao.incrementAvailableSeats(busId));
    }

    public LiveData<Integer> getAvailableSeats(int busId) {
        return busDao.getAvailableSeats(busId);
    }

    public LiveData<boolean[]> getSeatStatus(int busId) {
        return busDao.getSeatStatus(busId);
    }

    public void updateSeatStatus(int busId, boolean[] seatStatus) {
        executorService.execute(() -> busDao.updateSeatStatus(busId, seatStatus));
    }

    public void bookSeat(int busId, int seatNumber) {
        executorService.execute(() -> {
            Bus bus = busDao.getBusById(busId).getValue();
            if (bus != null) {
                boolean[] seatStatus = bus.getSeatStatus();
                if (seatStatus != null && seatNumber > 0 && seatNumber <= seatStatus.length) {
                    seatStatus[seatNumber - 1] = false;
                    busDao.updateSeatStatus(busId, seatStatus);
                    busDao.decrementAvailableSeats(busId);
                }
            }
        });
    }

    public void releaseSeat(int busId, int seatNumber) {
        executorService.execute(() -> {
            Bus bus = busDao.getBusById(busId).getValue();
            if (bus != null) {
                boolean[] seatStatus = bus.getSeatStatus();
                if (seatStatus != null && seatNumber > 0 && seatNumber <= seatStatus.length) {
                    seatStatus[seatNumber - 1] = true;
                    busDao.updateSeatStatus(busId, seatStatus);
                    busDao.incrementAvailableSeats(busId);
                }
            }
        });
    }

    public LiveData<List<String>> getAllSources() {
        return busDao.getAllSources();
    }

    public LiveData<List<String>> getAllDestinations() {
        return busDao.getAllDestinations();
    }

    public LiveData<List<String>> getDestinationsForSource(String source) {
        return busDao.getDestinationsForSource(source);
    }
    
    public LiveData<List<String>> getSourcesForDestination(String destination) {
        return busDao.getSourcesForDestination(destination);
    }

    /**
     * Get all bookings for a specific bus on a specific date
     * @param busId The ID of the bus
     * @param journeyDate The date of travel in dd/MM/yyyy format
     * @return LiveData containing list of bookings for that date
     */
    public LiveData<List<Booking>> getBookingsByBusAndDate(int busId, String journeyDate) {
        return AppDatabase.getDatabase(getApplication()).bookingDao().getBookingsByBusAndDate(busId, journeyDate);
    }

    public boolean insertSync(Bus bus) {
        final boolean[] result = {false};
        executorService.execute(() -> {
            result[0] = busDao.insertSync(bus) > 0;
        });
        return result[0];
    }

    public boolean[] getBookedSeatsForDateSync(int busId, String journeyDate) {
        final boolean[][] result = {null};
        executorService.execute(() -> {
            List<Booking> bookings = busDao.getBookingsByBusAndDateSync(busId, journeyDate);
            boolean[] seatStatus = new boolean[30];
            Arrays.fill(seatStatus, true);
            for (Booking booking : bookings) {
                int seatNumber = booking.getSeatNumber();
                if (seatNumber > 0 && seatNumber <= seatStatus.length) {
                    seatStatus[seatNumber - 1] = false;
                }
            }
            result[0] = seatStatus;
        });
        return result[0];
    }
} 