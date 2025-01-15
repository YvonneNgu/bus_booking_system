package com.example.bus_booking_system.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.databinding.FragmentSearchBinding;
import com.example.bus_booking_system.ui.BusDetailsActivity;
import com.example.bus_booking_system.ui.adapter.BusAdapter;
import com.example.bus_booking_system.viewmodel.BusViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SearchFragment extends Fragment implements BusAdapter.OnBusClickListener {
    private FragmentSearchBinding binding;
    private BusViewModel busViewModel;
    private BusAdapter adapter;
    private Calendar selectedDate = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

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
        setupClickListeners();
    }

    private void setupViewModel() {
        busViewModel = new ViewModelProvider(this).get(BusViewModel.class);

        busViewModel.insert(new Bus("KTM001", "Sri Maju Express", "Kuala Lumpur", "Penang", "08:00", "13:00", 45.00, 40, 35, "AC"));
        busViewModel.insert(new Bus("KTM002", "Transnasional", "Kuala Lumpur", "Johor Bahru", "09:30", "15:30", 55.00, 44, 44, "AC"));
        busViewModel.insert(new Bus("KTM003", "Plusliner", "Penang", "Kuala Lumpur", "07:00", "12:00", 45.00, 40, 38, "AC"));
        busViewModel.insert(new Bus("KTM004", "Aeroline", "Kuala Lumpur", "Singapore", "10:00", "16:00", 75.00, 32, 30, "AC"));
        busViewModel.insert(new Bus("KTM005", "Nice Express", "Ipoh", "Kuala Lumpur", "06:30", "09:30", 35.00, 44, 40, "Non-AC"));

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
        String source = binding.sourceInput.getText().toString().trim();
        String destination = binding.destinationInput.getText().toString().trim();
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
        // Same as onBusClick for now
        onBusClick(bus);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 