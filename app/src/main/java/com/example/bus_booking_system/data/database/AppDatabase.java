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

@Database(entities = {User.class, Bus.class, Booking.class}, version = 2, exportSchema = false)
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
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
} 