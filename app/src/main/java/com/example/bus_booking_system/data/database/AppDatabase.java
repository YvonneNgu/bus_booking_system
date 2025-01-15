package com.example.bus_booking_system.data.database;

import android.content.Context;
import android.os.AsyncTask;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.bus_booking_system.data.model.Bus;
import com.example.bus_booking_system.data.model.Booking;
import com.example.bus_booking_system.data.model.User;
import com.example.bus_booking_system.data.model.BooleanArrayConverter;

@Database(entities = {User.class, Bus.class, Booking.class}, version = 3, exportSchema = false)
@TypeConverters({BooleanArrayConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    
    public abstract UserDao userDao();
    public abstract BusDao busDao();
    public abstract BookingDao bookingDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add seatStatus column to buses table
            database.execSQL("ALTER TABLE buses ADD COLUMN seatStatus TEXT");
            database.execSQL("UPDATE buses SET seatStatus = '1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1' WHERE seatStatus IS NULL");

            // Add seatStatus column to bookings table
            database.execSQL("ALTER TABLE bookings ADD COLUMN seatStatus TEXT DEFAULT 'AVAILABLE'");

            // Create indices for bookings table
            database.execSQL("CREATE INDEX IF NOT EXISTS index_bookings_userId ON bookings(userId)");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_bookings_busId ON bookings(busId)");
        }
    };
    
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "bus_booking_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(INSTANCE).execute();
        }

        @Override
        public void onOpen(SupportSQLiteDatabase db) {
            super.onOpen(db);
            // Optional: You can also populate data when database is opened
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final BusDao busDao;

        PopulateDbAsync(AppDatabase db) {
            busDao = db.busDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Add sample bus routes
            
            // KL to Penang Route
            busDao.insert(new Bus("KL001", "Sri Maju Express", "Kuala Lumpur", "Penang", 
                "08:00", "13:00", 45.00, 30, 30));
            busDao.insert(new Bus("KL002", "Nice Express", "Kuala Lumpur", "Penang", 
                "10:30", "15:30", 45.00, 30, 30));
            busDao.insert(new Bus("KL003", "Transnasional", "Kuala Lumpur", "Penang", 
                "14:00", "19:00", 42.00, 30, 30));
            busDao.insert(new Bus("KL004", "Plusliner", "Kuala Lumpur", "Penang", 
                "20:00", "01:00", 48.00, 30, 30));

            // KL to JB Route
            busDao.insert(new Bus("KL005", "Sri Maju Express", "Kuala Lumpur", "Johor Bahru", 
                "07:00", "13:00", 55.00, 30, 30));
            busDao.insert(new Bus("KL006", "Transnasional", "Kuala Lumpur", "Johor Bahru", 
                "09:30", "15:30", 52.00, 30, 30));
            busDao.insert(new Bus("KL007", "Plusliner", "Kuala Lumpur", "Johor Bahru", 
                "13:00", "19:00", 55.00, 30, 30));
            busDao.insert(new Bus("KL008", "Nice Express", "Kuala Lumpur", "Johor Bahru", 
                "22:00", "04:00", 58.00, 30, 30));

            // KL to Melaka Route
            busDao.insert(new Bus("KL009", "Sri Maju Express", "Kuala Lumpur", "Melaka", 
                "08:30", "11:30", 25.00, 30, 30));
            busDao.insert(new Bus("KL010", "Transnasional", "Kuala Lumpur", "Melaka", 
                "11:00", "14:00", 23.00, 30, 30));
            busDao.insert(new Bus("KL011", "Nice Express", "Kuala Lumpur", "Melaka", 
                "14:30", "17:30", 25.00, 30, 30));

            // KL to Ipoh Route
            busDao.insert(new Bus("KL012", "Plusliner", "Kuala Lumpur", "Ipoh", 
                "07:30", "10:30", 35.00, 30, 30));
            busDao.insert(new Bus("KL013", "Sri Maju Express", "Kuala Lumpur", "Ipoh", 
                "10:00", "13:00", 33.00, 30, 30));
            busDao.insert(new Bus("KL014", "Nice Express", "Kuala Lumpur", "Ipoh", 
                "15:30", "18:30", 35.00, 30, 30));

            // Return routes
            // Penang to KL
            busDao.insert(new Bus("PG001", "Sri Maju Express", "Penang", "Kuala Lumpur", 
                "08:00", "13:00", 45.00, 30, 30));
            busDao.insert(new Bus("PG002", "Transnasional", "Penang", "Kuala Lumpur", 
                "14:00", "19:00", 42.00, 30, 30));

            // JB to KL
            busDao.insert(new Bus("JB001", "Plusliner", "Johor Bahru", "Kuala Lumpur", 
                "07:00", "13:00", 55.00, 30, 30));
            busDao.insert(new Bus("JB002", "Sri Maju Express", "Johor Bahru", "Kuala Lumpur", 
                "20:00", "02:00", 58.00, 30, 30));

            // Melaka to KL
            busDao.insert(new Bus("MK001", "Nice Express", "Melaka", "Kuala Lumpur", 
                "09:00", "12:00", 25.00, 30, 30));
            busDao.insert(new Bus("MK002", "Transnasional", "Melaka", "Kuala Lumpur", 
                "15:00", "18:00", 23.00, 30, 30));

            // Ipoh to KL
            busDao.insert(new Bus("IP001", "Sri Maju Express", "Ipoh", "Kuala Lumpur", 
                "08:30", "11:30", 35.00, 30, 30));
            busDao.insert(new Bus("IP002", "Plusliner", "Ipoh", "Kuala Lumpur", 
                "16:30", "19:30", 33.00, 30, 30));

            return null;
        }
    }
} 