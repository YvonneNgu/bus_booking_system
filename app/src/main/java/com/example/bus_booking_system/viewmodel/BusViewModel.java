package com.example.bus_booking_system.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.data.repository.BusRepository;

import java.util.List;

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
} 