package com.example.bus_booking_system.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bus_booking_system.R;
import com.example.bus_booking_system.data.model.Booking;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {

    private List<Booking> bookings;

    public BookingsAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView bookingDateTextView;
        private TextView journeyDateTextView;
        private TextView seatNumberTextView;
        private TextView statusTextView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingDateTextView = itemView.findViewById(R.id.bookingDateTextView);
            journeyDateTextView = itemView.findViewById(R.id.journeyDateTextView);
            seatNumberTextView = itemView.findViewById(R.id.seatNumberTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }

        public void bind(Booking booking) {
            bookingDateTextView.setText("Booking Date       : "+booking.getBookingDate());
            journeyDateTextView.setText("Journey Date       : "+booking.getJourneyDate());
            seatNumberTextView.setText("Seat Number       : "+ booking.getSeatNumber());
                statusTextView.setText("Status                   : "+booking.getStatus());
        }
    }
}