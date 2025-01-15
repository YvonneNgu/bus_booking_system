package com.example.bus_booking_system.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.databinding.ItemBusBinding;

public class BusAdapter extends ListAdapter<Bus, BusAdapter.BusViewHolder> {
    private final OnBusClickListener listener;

    public BusAdapter(OnBusClickListener listener) {
        super(new BusDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBusBinding binding = ItemBusBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new BusViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class BusViewHolder extends RecyclerView.ViewHolder {
        private final ItemBusBinding binding;

        BusViewHolder(ItemBusBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Bus bus) {
            binding.busNameText.setText(bus.getBusName());
            binding.busTypeText.setText(bus.getBusType());
            binding.departureTimeText.setText(bus.getDepartureTime());
            binding.arrivalTimeText.setText(bus.getArrivalTime());
            binding.sourceText.setText(bus.getSource());
            binding.destinationText.setText(bus.getDestination());
            binding.fareText.setText(String.format("₹%.2f", bus.getFare()));
            binding.seatsText.setText(String.format("%d seats available", bus.getAvailableSeats()));

            binding.bookButton.setOnClickListener(v -> listener.onBookClick(bus));
            itemView.setOnClickListener(v -> listener.onBusClick(bus));
        }
    }

    public interface OnBusClickListener {
        void onBusClick(Bus bus);
        void onBookClick(Bus bus);
    }

    private static class BusDiffCallback extends DiffUtil.ItemCallback<Bus> {
        @Override
        public boolean areItemsTheSame(@NonNull Bus oldItem, @NonNull Bus newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Bus oldItem, @NonNull Bus newItem) {
            return oldItem.equals(newItem);
        }
    }
} 