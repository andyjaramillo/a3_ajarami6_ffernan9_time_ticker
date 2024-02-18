package com.example.a3_ajarami6_ffernan9;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Locale;

public class TimeZoneConverter {

    private static final HashMap<String, Integer> TIME_ZONES;
    static {
        TIME_ZONES = new HashMap<>();
        TIME_ZONES.put("America/New_York", -5);
        TIME_ZONES.put("America/Los_Angeles", -8);
        TIME_ZONES.put("Europe/Berlin", +1);
        TIME_ZONES.put("Europe/Istanbul", +2);
        TIME_ZONES.put("Asia/Singapore", +8);
        TIME_ZONES.put("Asia/Tokyo", +9);
        TIME_ZONES.put("Australia/Canberra", +10);
        TIME_ZONES.put("Europe/London", +0);
    }

    @NonNull
    public static String convertToGMT(String timeZone) {
        Integer gmtOffset = TIME_ZONES.get(timeZone);
        if (gmtOffset == null) {
            return "Unknown";
        }
        String gmtTime = "GMT " + (gmtOffset < 0 ? "-" : "+");
        gmtTime += String.format(Locale.US, "%02d:00", Math.abs(gmtOffset));
        return gmtTime;
    }
}
