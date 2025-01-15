package com.example.bus_booking_system.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bus_booking_system.R;

public class SeatSelectionActivity extends AppCompatActivity {

    private GridLayout seatGrid;
    private Button selectedSeatButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        seatGrid = findViewById(R.id.seatGrid);

        setupSeats();
    }

    private void setupSeats() {
        int totalSeats = 30; // 10 rows, 3 seats per row
        for (int i = 0; i < totalSeats; i++) {
            Button seatButton = new Button(this);
            seatButton.setLayoutParams(new GridLayout.LayoutParams());
            seatButton.setBackgroundColor(getResources().getColor(android.R.color.white));
            seatButton.setOnClickListener(this::onSeatClick);
            seatGrid.addView(seatButton);
        }
    }

    private void onSeatClick(View view) {
        if (selectedSeatButton != null) {
            selectedSeatButton.setBackgroundColor(getResources().getColor(android.R.color.white));
        }
        selectedSeatButton = (Button) view;
        selectedSeatButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        Toast.makeText(this, "Seat Selected", Toast.LENGTH_SHORT).show();
    }
}