package com.example.bus_booking_system.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bus_booking_system.data.model.Booking;
import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.databinding.ActivityBusDetailsBinding;
import com.example.bus_booking_system.utils.SessionManager;
import com.example.bus_booking_system.viewmodel.BookingViewModel;
import com.example.bus_booking_system.viewmodel.BusViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bus Details");

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

        if (busId == -1) {
            Toast.makeText(this, "Invalid bus details", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        busViewModel.getBusById(busId).observe(this, bus -> {
            if (bus != null) {
                currentBus = bus;
                updateUI(bus);
                setupSeats(bus.getTotalSeats());
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

    private void setupSeats(int totalSeats) {
        seatButtons = new MaterialButton[totalSeats];
        binding.seatsContainer.removeAllViews();

        for (int i = 0; i < totalSeats; i++) {
            MaterialButton seatButton = new MaterialButton(this, null, 
                    com.google.android.material.R.attr.materialButtonOutlinedStyle);
            seatButton.setText(String.valueOf(i + 1));
            seatButton.setTag(i + 1);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);
            params.columnSpec = GridLayout.spec(i % 4, 1f);
            params.rowSpec = GridLayout.spec(i / 4, 1f);
            seatButton.setLayoutParams(params);

            final int seatNumber = i + 1;
            seatButton.setOnClickListener(v -> onSeatSelected(seatNumber));

            seatButtons[i] = seatButton;
            binding.seatsContainer.addView(seatButton);
        }
    }

    private void loadBookedSeats(int busId) {
        bookingViewModel.getBookingsByBusAndDate(busId, journeyDate)
                .observe(this, bookings -> {
                    for (Booking booking : bookings) {
                        if (booking.getStatus().equals("CONFIRMED")) {
                            int seatNumber = booking.getSeatNumber();
                            seatButtons[seatNumber - 1].setEnabled(false);
                            seatButtons[seatNumber - 1].setBackgroundColor(
                                    getResources().getColor(android.R.color.darker_gray));
                        }
                    }
                });
    }

    private void onSeatSelected(int seatNumber) {
        if (selectedSeat != -1) {
            seatButtons[selectedSeat - 1].setChecked(false);
        }

        selectedSeat = seatNumber;
        seatButtons[seatNumber - 1].setChecked(true);
        binding.selectedSeatText.setText("Seat " + seatNumber);
        binding.proceedButton.setEnabled(true);
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

        Booking booking = new Booking(
                sessionManager.getUserId(),
                currentBus.getId(),
                currentDate,
                journeyDate,
                selectedSeat,
                currentBus.getFare()
        );

        bookingViewModel.insert(booking);
        busViewModel.decreaseAvailableSeats(currentBus.getId());

        /*Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("booking_id", booking.getId());
        startActivity(intent);
        finish();*/
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