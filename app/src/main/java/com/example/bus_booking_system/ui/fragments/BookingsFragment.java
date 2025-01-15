package com.example.bus_booking_system.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bus_booking_system.databinding.FragmentBookingsBinding;
import com.example.bus_booking_system.ui.adapter.BookingsAdapter;
import com.example.bus_booking_system.utils.SessionManager;
import com.example.bus_booking_system.viewmodel.BookingViewModel;

import java.util.ArrayList;

public class BookingsFragment extends Fragment {
    private FragmentBookingsBinding binding;
    private BookingViewModel bookingViewModel;
    private SessionManager sessionManager;
    private BookingsAdapter bookingsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupUI();
    }

    private void setupViewModel() {
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        sessionManager = new SessionManager(requireContext());
    }

    private void setupUI() {
        binding.bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bookingsAdapter = new BookingsAdapter(new ArrayList<>());
        binding.bookingsRecyclerView.setAdapter(bookingsAdapter);

        int userId = sessionManager.getUserId();
        bookingViewModel.getUserBookings(userId).observe(getViewLifecycleOwner(), bookings -> {
            bookingsAdapter.setBookings(bookings);
            binding.emptyView.setVisibility(bookings.isEmpty() ? View.VISIBLE : View.GONE);
            binding.bookingsRecyclerView.setVisibility(bookings.isEmpty() ? View.GONE : View.VISIBLE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}