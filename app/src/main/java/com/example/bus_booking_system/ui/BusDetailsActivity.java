package com.example.bus_booking_system.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.bus_booking_system.R;
import com.example.bus_booking_system.data.model.Booking;
import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.data.repository.BookingRepository;
import com.example.bus_booking_system.databinding.ActivityBusDetailsBinding;
import com.example.bus_booking_system.utils.SessionManager;
import com.example.bus_booking_system.viewmodel.BookingViewModel;
import com.example.bus_booking_system.viewmodel.BusViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BusDetailsActivity extends AppCompatActivity {
    private ActivityBusDetailsBinding binding;
    private BusViewModel busViewModel;
    private BookingViewModel bookingViewModel;
    private SessionManager sessionManager;
    private Bus currentBus;
    private String journeyDate;
    private int selectedSeat = -1;
    private MaterialButton[] seatButtons;
    private List<Integer> bookedSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Select Seat");
        }

        setupViewModels();
        loadBusDetails();
        setupClickListeners();
    }

    private void setupViewModels() {
        busViewModel = new ViewModelProvider(this).get(BusViewModel.class);
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        sessionManager = new SessionManager(this);
    }

    private void loadBusDetails() {
        int busId = getIntent().getIntExtra("bus_id", -1);
        journeyDate = getIntent().getStringExtra("journey_date");
        System.out.println("INITIAL DATE: " + journeyDate);

        if (busId == -1) {
            Toast.makeText(this, "Invalid bus details", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        busViewModel.getBusById(busId).observe(this, bus -> {
            if (bus != null) {
                currentBus = bus;
                updateUI(bus);
                initializeSeatGrid();
                loadBookedSeats(bus.getId());
            }
        });
    }

    private void updateUI(Bus bus) {
        binding.busNameText.setText(bus.getBusName());
        binding.busTypeText.setText(bus.getBusType());
        binding.departureTimeText.setText(bus.getDepartureTime());
        binding.arrivalTimeText.setText(bus.getArrivalTime());
        binding.sourceText.setText(bus.getSource());
        binding.destinationText.setText(bus.getDestination());
        binding.fareText.setText(String.format("₹%.2f", bus.getFare()));
    }

    private void initializeSeatGrid() {
        GridLayout seatGrid = binding.seatsContainer;
        seatGrid.removeAllViews(); // Clear existing views
        
        int totalSeats = currentBus != null ? currentBus.getTotalSeats() : 30;
        seatButtons = new MaterialButton[totalSeats];
        
        for (int i = 0; i < totalSeats; i++) {
            MaterialButton seatButton = new MaterialButton(this);
            seatButton.setText(String.valueOf(i + 1));
            seatButton.setTag(i + 1);
            
            // Set layout parameters for the button
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);
            seatButton.setLayoutParams(params);
            
            // Add click listener
            final int seatNumber = i + 1;
            seatButton.setOnClickListener(v -> onSeatSelected(seatNumber));
            
            seatButtons[i] = seatButton;
            seatGrid.addView(seatButton);
        }
    }

    private void loadBookedSeats(int busId) {
        busViewModel.getSeatStatus(busId).observe(this, seatStatus -> {
            if (seatStatus != null) {
                for (int i = 0; i < seatStatus.length; i++) {
                    MaterialButton seatButton = seatButtons[i];
                    if (!seatStatus[i]) { // Seat is booked
                        seatButton.setEnabled(false);
                        seatButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    } else {
                        seatButton.setEnabled(true);
//                        seatButton.setBackgroundColor(getResources().getColor(android.R.color.white));
                    }
                }
            }
        });
    }

    private void onSeatSelected(int seatNumber) {
        // Reset the background color of the previously selected seat
        if (selectedSeat != -1) {
            seatButtons[selectedSeat - 1].setBackgroundColor(getThemeColor(this, com.google.android.material.R.attr.colorPrimary));
        }

        // Set the new selected seat
        selectedSeat = seatNumber;
        seatButtons[seatNumber - 1].setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
        binding.selectedSeatText.setText("Seat " + seatNumber);
        binding.proceedButton.setEnabled(true);
    }

    public int getThemeColor(Context context, int colorAttribute) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttribute, typedValue, true);
        return typedValue.data;
    }


    private void setupClickListeners() {
        binding.proceedButton.setOnClickListener(v -> proceedToBooking());
    }

    private void proceedToBooking() {
        if (selectedSeat == -1) {
            Toast.makeText(this, "Please select a seat", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date());

        System.out.println("JourneyDateeeeeee "  + journeyDate);
        Booking booking = new Booking(
                sessionManager.getUserId(),
                currentBus.getId(),
                currentDate,
                journeyDate,
                selectedSeat,
                currentBus.getFare()
        );

        bookingViewModel.insert(booking, new BookingRepository.BookingCallback() {
            @Override
            public void onSuccess(long bookingId) {
                runOnUiThread(() -> {
                    busViewModel.bookSeat(currentBus.getId(), selectedSeat);
                    Toast.makeText(BusDetailsActivity.this, "Booking successful!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(BusDetailsActivity.this, "Booking failed: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 