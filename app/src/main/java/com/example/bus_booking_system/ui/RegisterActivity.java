package com.example.bus_booking_system.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bus_booking_system.data.model.User;
import com.example.bus_booking_system.databinding.ActivityRegisterBinding;
import com.example.bus_booking_system.viewmodel.UserViewModel;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.registerButton.setOnClickListener(v -> attemptRegistration());
        binding.loginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegistration() {
        String name = binding.nameInput.getText().toString().trim();
        String email = binding.emailInput.getText().toString().trim();
        String phone = binding.phoneInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.setError("Please enter a valid email address");
            return;
        }

        if (password.length() < 6) {
            binding.passwordLayout.setError("Password must be at least 6 characters");
            return;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            binding.phoneLayout.setError("Please enter a valid phone number");
            return;
        }

        // Check if email already exists
        userViewModel.getUserByEmail(email).observe(this, existingUser -> {
            if (existingUser != null) {
                binding.emailLayout.setError("Email already registered");
                return;
            }

            // Create new user
            User newUser = new User(name, email, phone, password);
            userViewModel.insert(newUser);

            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
} 