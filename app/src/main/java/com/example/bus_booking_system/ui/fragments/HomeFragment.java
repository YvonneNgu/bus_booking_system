package com.example.bus_booking_system.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.databinding.FragmentHomeBinding;
import com.example.bus_booking_system.ui.adapter.BusAdapter;
import com.example.bus_booking_system.viewmodel.BusViewModel;

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

            }

            @Override
            public void onBookClick(Bus bus) {
                // Handle book click
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