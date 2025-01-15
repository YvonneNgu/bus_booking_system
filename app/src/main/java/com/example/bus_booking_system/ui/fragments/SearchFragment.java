package com.example.bus_booking_system.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
        sourceInput = binding.sourceInput;
        destinationInput = binding.destinationInput;

        sourceAdapter = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_dropdown_item_1line);
        destinationAdapter = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_dropdown_item_1line);

        sourceInput.setAdapter(sourceAdapter);
        destinationInput.setAdapter(destinationAdapter);

        // Add listeners for source selection
        sourceInput.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSource = sourceAdapter.getItem(position);
            updateDestinationsForSource(selectedSource);
            // Clear destination when source changes
            destinationInput.setText("", false);
        });

        // Add listeners for destination selection
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
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    private void performSearch() {
        String source = sourceInput.getText().toString().trim();
        String destination = destinationInput.getText().toString().trim();
        String date = binding.dateInput.getText().toString().trim();

        if (source.isEmpty() || destination.isEmpty() || date.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressIndicator.setVisibility(View.VISIBLE);
        busViewModel.searchBuses(source, destination).observe(getViewLifecycleOwner(), buses -> {
            binding.progressIndicator.setVisibility(View.GONE);
            adapter.submitList(buses);
        });
    }

    @Override
    public void onBusClick(Bus bus) {
        Intent intent = new Intent(requireContext(), BusDetailsActivity.class);
        intent.putExtra("bus_id", bus.getId());
        intent.putExtra("journey_date", binding.dateInput.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onBookClick(Bus bus) {
        onBusClick(bus);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 