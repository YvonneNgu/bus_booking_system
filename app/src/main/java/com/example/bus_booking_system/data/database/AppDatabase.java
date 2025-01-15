package com.example.bus_booking_system.data.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.data.model.Booking;
import com.example.bus_booking_system.data.model.User;

@Database(entities = {User.class, Bus.class, Booking.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    
    public abstract UserDao userDao();
    public abstract BusDao busDao();
    public abstract BookingDao bookingDao();
    
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "bus_booking_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@androidx.annotation.NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    new PopulateDbAsync(INSTANCE).execute();
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final BusDao busDao;
        private final BookingDao bookingDao;

        PopulateDbAsync(AppDatabase db) {
            busDao = db.busDao();
            bookingDao = db.bookingDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            busDao.insert(new Bus("KTM001", "Sri Maju Express", "Kuala Lumpur", "Penang", "08:00", "13:00", 45.00, 40, 35, "AC"));
            busDao.insert(new Bus("KTM002", "Transnasional", "Kuala Lumpur", "Johor Bahru", "09:30", "15:30", 55.00, 44, 44, "AC"));
            busDao.insert(new Bus("KTM003", "Plusliner", "Penang", "Kuala Lumpur", "07:00", "12:00", 45.00, 40, 38, "AC"));
            busDao.insert(new Bus("KTM004", "Aeroline", "Kuala Lumpur", "Singapore", "10:00", "16:00", 75.00, 32, 30, "AC"));
            busDao.insert(new Bus("KTM005", "Nice Express", "Ipoh", "Kuala Lumpur", "06:30", "09:30", 35.00, 44, 40, "Non-AC"));

            bookingDao.insert(new Booking(1, 1, "2025-01-14", "2025-01-20", 12, 45.00, "CONFIRMED", "PAID"));
            bookingDao.insert(new Booking(2, 1, "2025-01-14", "2025-01-20", 13, 45.00, "CONFIRMED", "PAID"));
            bookingDao.insert(new Booking(3, 2, "2025-01-14", "2025-01-21", 5, 55.00, "PENDING", "PENDING"));
            bookingDao.insert(new Booking(4, 3, "2025-01-13", "2025-01-19", 22, 45.00, "CANCELLED", "PAID"));
            bookingDao.insert(new Booking(5, 4, "2025-01-15", "2025-01-22", 15, 75.00, "CONFIRMED", "PAID"));
            bookingDao.insert(new Booking(6, 5, "2025-01-15", "2025-01-18", 33, 35.00, "CONFIRMED", "PENDING"));

            return null;
        }
    }
} 