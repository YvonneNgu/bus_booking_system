package com.example.bus_booking_system.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bus_booking_system.data.database.AppDatabase;
import com.example.bus_booking_system.data.database.BusDao;
import com.example.bus_booking_system.data.model.Bus;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BusRepository {
    private BusDao busDao;
    private LiveData<List<Bus>> allBuses;
    private ExecutorService executorService;

    public BusRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        busDao = db.busDao();
        allBuses = busDao.getAllBuses();
        executorService = Executors.newSingleThreadExecutor();
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
        executorService.execute(() -> busDao.decreaseAvailableSeats(busId));
    }

    public void increaseAvailableSeats(int busId) {
        executorService.execute(() -> busDao.increaseAvailableSeats(busId));
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
                    busDao.decreaseAvailableSeats(busId);
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
                    busDao.increaseAvailableSeats(busId);
                }
            }
        });
    }
} 