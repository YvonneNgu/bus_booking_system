package com.example.bus_booking_system.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.databinding.FragmentHomeBinding;
import com.example.bus_booking_system.ui.BusDetailsActivity;
import com.example.bus_booking_system.ui.adapter.BusAdapter;
import com.example.bus_booking_system.viewmodel.BusViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private BusViewModel busViewModel;
    private BusAdapter busAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupUI();
        fetchPopularRoutes();
    }

    private void setupViewModel() {
        busViewModel = new ViewModelProvider(this).get(BusViewModel.class);
    }

    private void setupUI() {
        busAdapter = new BusAdapter(new BusAdapter.OnBusClickListener() {
            @Override
            public void onBusClick(Bus bus) {
                String todayDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                // Pass selected bus details and taday's date to BusDetailsActivity
                Intent intent = new Intent(requireContext(), BusDetailsActivity.class);
                intent.putExtra("bus_id", bus.getId());
                intent.putExtra("journey_date", todayDate);
                intent.putExtra("bus_number", bus.getBusNumber());
                intent.putExtra("available_seats", bus.getAvailableSeats());
                intent.putExtra("departure_time", bus.getDepartureTime());
                intent.putExtra("arrival_time", bus.getArrivalTime());
                intent.putExtra("destination", bus.getDestination());
                intent.putExtra("source", bus.getSource());
                // Add logging to verify the data being passed
                Log.d("SearchFragment", "Sending bus_id: " + bus.getId());
                Log.d("SearchFragment", "Sending journey_date: " + todayDate);

                startActivity(intent);
            }
        });

        binding.popularRoutesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.popularRoutesRecyclerView.setAdapter(busAdapter);
    }

    private void fetchPopularRoutes() {
        busViewModel.getAllBuses().observe(getViewLifecycleOwner(), buses -> {
            // Assuming all buses are popular routes for now
            busAdapter.submitList(buses);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 