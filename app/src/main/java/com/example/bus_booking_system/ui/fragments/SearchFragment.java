package com.example.bus_booking_system.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.databinding.FragmentSearchBinding;
import com.example.bus_booking_system.ui.BusDetailsActivity;
import com.example.bus_booking_system.ui.adapter.BusAdapter;
import com.example.bus_booking_system.viewmodel.BusViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * SearchFragment handles the bus search functionality
 * Flow:
 * 1. User selects source and destination from dropdowns
 * 2. User selects journey date using date picker
 * 3. On search, displays available buses
 * 4. On bus selection, passes details including journey date to BusDetailsActivity
 */
public class SearchFragment extends Fragment implements BusAdapter.OnBusClickListener {
    private FragmentSearchBinding binding;
    private BusViewModel busViewModel;
    private BusAdapter adapter;
    private Calendar selectedDate = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private AutoCompleteTextView sourceInput;
    private AutoCompleteTextView destinationInput;
    private ArrayAdapter<String> sourceAdapter;
    private ArrayAdapter<String> destinationAdapter;
    private String selectedJourneyDate; // Store selected journey date

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupRecyclerView();
        setupSpinners();
        setupClickListeners();
        loadInitialData();
    }

    private void setupViewModel() {
        busViewModel = new ViewModelProvider(this).get(BusViewModel.class);
    }

    private void setupSpinners() {
        // Initialize source and destination dropdowns
        sourceInput = binding.sourceInput;
        destinationInput = binding.destinationInput;

        sourceAdapter = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_dropdown_item_1line);
        destinationAdapter = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_dropdown_item_1line);

        sourceInput.setAdapter(sourceAdapter);
        destinationInput.setAdapter(destinationAdapter);

        // Update destinations when source is selected
        sourceInput.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSource = sourceAdapter.getItem(position);
            updateDestinationsForSource(selectedSource);
            destinationInput.setText("", false);
        });

        // Update sources when destination is selected (if source is empty)
        destinationInput.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDestination = destinationAdapter.getItem(position);
            updateSourcesForDestination(selectedDestination);
        });
    }

    private void loadInitialData() {
        // Load initial sources
        busViewModel.getAllSources().observe(getViewLifecycleOwner(), this::updateSourceAdapter);
    }

    private void updateDestinationsForSource(String source) {
        busViewModel.getDestinationsForSource(source).observe(getViewLifecycleOwner(), 
            this::updateDestinationAdapter);
    }

    private void updateSourcesForDestination(String destination) {
        if (sourceInput.getText().toString().isEmpty()) {
            busViewModel.getSourcesForDestination(destination).observe(getViewLifecycleOwner(), 
                this::updateSourceAdapter);
        }
    }

    private void updateSourceAdapter(List<String> sources) {
        sourceAdapter.clear();
        sourceAdapter.addAll(sources);
    }

    private void updateDestinationAdapter(List<String> destinations) {
        destinationAdapter.clear();
        destinationAdapter.addAll(destinations);
    }

    private void setupRecyclerView() {
        adapter = new BusAdapter(this);
        binding.busRecyclerView.setAdapter(adapter);
        binding.busRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupClickListeners() {
        binding.searchButton.setOnClickListener(v -> performSearch());
        binding.dateInput.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    binding.dateInput.setText(dateFormat.format(selectedDate.getTime()));
                    System.out.println("binding.dateInput");
                    System.out.println(binding.dateInput.getText().toString());
                    selectedJourneyDate = dateFormat.format(selectedDate.getTime());
                    binding.dateInput.setText(selectedJourneyDate);
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        // Set minimum date to today
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    private void performSearch() {
        String source = sourceInput.getText().toString().trim();
        String destination = destinationInput.getText().toString().trim();

        // Validate all fields are filled
        if (source.isEmpty() || destination.isEmpty()) {
            Toast.makeText(requireContext(), "Please select source and destination", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedJourneyDate == null || selectedJourneyDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please select journey date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator and hide other views
        binding.progressIndicator.setVisibility(View.VISIBLE);
        binding.busRecyclerView.setVisibility(View.GONE);
        binding.noResultsText.setVisibility(View.GONE);

        // Search for buses
        busViewModel.searchBuses(source, destination).observe(getViewLifecycleOwner(), buses -> {
            binding.progressIndicator.setVisibility(View.GONE);

            if (buses != null && !buses.isEmpty()) {
                adapter.submitList(buses);
                binding.busRecyclerView.setVisibility(View.VISIBLE);
                binding.noResultsText.setVisibility(View.GONE);
            } else {
                binding.busRecyclerView.setVisibility(View.GONE);
                binding.noResultsText.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBusClick(Bus bus) {
        // Validate journey date before proceeding
        if (selectedJourneyDate == null || selectedJourneyDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please select journey date first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass selected bus details and journey date to BusDetailsActivity
        Intent intent = new Intent(requireContext(), BusDetailsActivity.class);

        intent.putExtra("bus_number", bus.getBusNumber());
        intent.putExtra("available_seats", bus.getAvailableSeats());
        intent.putExtra("bus_id", bus.getId());
        intent.putExtra("departure_time", bus.getDepartureTime());
        intent.putExtra("arrival_time", bus.getArrivalTime());
        intent.putExtra("destination", bus.getDestination());
        intent.putExtra("source", bus.getSource());
        intent.putExtra("journey_date", selectedJourneyDate);

        // Add logging to verify the data being passed
        Log.d("SearchFragment", "Sending bus_id: " + bus.getId());
        Log.d("SearchFragment", "Sending journey_date: " + selectedJourneyDate);

        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 