package com.example.bus_booking_system.data.model;

import androidx.room.TypeConverter;

// Type converter for boolean array
public class BooleanArrayConverter {
    @TypeConverter
    public static String fromBooleanArray(boolean[] array) {
        if (array == null) return null;
        StringBuilder sb = new StringBuilder();
        for (boolean b : array) {
            sb.append(b ? '1' : '0');
        }
        return sb.toString();
    }

    @TypeConverter
    public static boolean[] toBooleanArray(String string) {
        if (string == null) return null;
        boolean[] array = new boolean[string.length()];
        for (int i = 0; i < string.length(); i++) {
            array[i] = string.charAt(i) == '1';
        }
        return array;
    }
}
