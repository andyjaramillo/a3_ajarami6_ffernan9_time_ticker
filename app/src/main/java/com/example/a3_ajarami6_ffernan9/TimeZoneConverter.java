package com.example.a3_ajarami6_ffernan9;

public class TimeZoneConverter {
    public static final String convertToGMT(String timezone) {
        String gmtTime = "";
        switch (timezone) {
            case "America/New_York":
                gmtTime = "GMT -05:00";
                break;
            case "America/Los_Angeles":
                gmtTime = "GMT -08:00";
                break;
            case "Europe/Berlin":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Istanbul":
                gmtTime = "GMT +02:00";
                break;
            case "Asia/Singapore":
                gmtTime = "GMT +08:00";
                break;
            case "Asia/Tokyo":
                gmtTime = "GMT +09:00";
                break;
            case "Australia/Canberra":
                gmtTime = "GMT +10:00";
                break;
            case "America/Chicago":
                gmtTime = "GMT -06:00";
                break;
            case "America/Denver":
                gmtTime = "GMT -07:00";
                break;
            case "America/Phoenix":
                gmtTime = "GMT -07:00";
                break;
            case "America/Indiana/Indianapolis":
                gmtTime = "GMT -05:00";
                break;
            case "America/Mexico_City":
                gmtTime = "GMT -06:00";
                break;
            case "America/Sao_Paulo":
                gmtTime = "GMT -03:00";
                break;
            case "America/Argentina/Buenos_Aires":
                gmtTime = "GMT -03:00";
                break;
            case "America/Toronto":
                gmtTime = "GMT -05:00";
                break;
            case "Europe/London":
                gmtTime = "GMT +00:00";
                break;
            case "Europe/Paris":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Rome":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Moscow":
                gmtTime = "GMT +03:00";
                break;
            case "Europe/Madrid":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Amsterdam":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Oslo":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Stockholm":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Athens":
                gmtTime = "GMT +02:00";
                break;
            case "Europe/Zurich":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Brussels":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Prague":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Vienna":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Budapest":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Warsaw":
                gmtTime = "GMT +01:00";
                break;
            case "Asia/Dubai":
                gmtTime = "GMT +04:00";
                break;
            case "Asia/Jerusalem":
                gmtTime = "GMT +02:00";
                break;
            case "Asia/Kolkata":
                gmtTime = "GMT +05:30";
                break;
            case "Asia/Shanghai":
                gmtTime = "GMT +08:00";
                break;
            case "Asia/Hong_Kong":
                gmtTime = "GMT +08:00";
                break;
            case "Asia/Seoul":
                gmtTime = "GMT +09:00";
                break;
            case "Asia/Jakarta":
                gmtTime = "GMT +07:00";
                break;
            case "Asia/Bangkok":
                gmtTime = "GMT +07:00";
                break;
            case "Asia/Kuala_Lumpur":
                gmtTime = "GMT +08:00";
                break;
            case "Asia/Manila":
                gmtTime = "GMT +08:00";
                break;
            case "Asia/Taipei":
                gmtTime = "GMT +08:00";
                break;
            case "Asia/Dhaka":
                gmtTime = "GMT +06:00";
                break;
            case "Asia/Colombo":
                gmtTime = "GMT +05:30";
                break;
            case "Asia/Kabul":
                gmtTime = "GMT +04:30";
                break;
            case "Asia/Tehran":
                gmtTime = "GMT +03:30";
                break;
            case "Asia/Baghdad":
                gmtTime = "GMT +03:00";
                break;
            case "Asia/Riyadh":
                gmtTime = "GMT +03:00";
                break;
            case "Asia/Kuwait":
                gmtTime = "GMT +03:00";
                break;
            case "Asia/Qatar":
                gmtTime = "GMT +03:00";
                break;
            case "Asia/Muscat":
                gmtTime = "GMT +04:00";
                break;
            case "Africa/Johannesburg":
                gmtTime = "GMT +02:00";
                break;
            case "Africa/Cairo":
                gmtTime = "GMT +02:00";
                break;
            case "Africa/Lagos":
                gmtTime = "GMT +01:00";
                break;
            case "Africa/Nairobi":
                gmtTime = "GMT +03:00";
                break;
            case "Africa/Casablanca":
                gmtTime = "GMT +00:00";
                break;
            case "Australia/Sydney":
                gmtTime = "GMT +11:00";
                break;
            case "Australia/Melbourne":
                gmtTime = "GMT +11:00";
                break;
            case "Australia/Brisbane":
                gmtTime = "GMT +10:00";
                break;
            case "Australia/Perth":
                gmtTime = "GMT +08:00";
                break;
            case "Australia/Adelaide":
                gmtTime = "GMT +10:30";
                break;
            case "Australia/Hobart":
                gmtTime = "GMT +11:00";
                break;
            case "Pacific/Auckland":
                gmtTime = "GMT +13:00";
                break;
            case "Pacific/Honolulu":
                gmtTime = "GMT -10:00";
                break;
            case "Pacific/Tahiti":
                gmtTime = "GMT -10:00";
                break;
            case "Pacific/Midway":
                gmtTime = "GMT -11:00";
                break;
            case "Pacific/Samoa":
                gmtTime = "GMT -11:00";
                break;
            case "Pacific/Niue":
                gmtTime = "GMT -11:00";
                break;
            case "Pacific/Pago_Pago":
                gmtTime = "GMT -11:00";
                break;
            case "America/Adak":
                gmtTime = "GMT -10:00";
                break;
            case "Pacific/Gambier":
                gmtTime = "GMT -09:00";
                break;
            case "Pacific/Pitcairn":
                gmtTime = "GMT -08:00";
                break;
            case "America/Anchorage":
                gmtTime = "GMT -09:00";
                break;
            case "America/Juneau":
                gmtTime = "GMT -09:00";
                break;
            case "America/Metlakatla":
                gmtTime = "GMT -09:00";
                break;
            case "America/Nome":
                gmtTime = "GMT -09:00";
                break;
            case "America/Sitka":
                gmtTime = "GMT -09:00";
                break;
            case "America/Yakutat":
                gmtTime = "GMT -09:00";
                break;
            case "Pacific/Marquesas":
                gmtTime = "GMT -09:30";
                break;
            case "America/Dawson":
                gmtTime = "GMT -08:00";
                break;
            case "America/Tijuana":
                gmtTime = "GMT -08:00";
                break;
            case "America/Vancouver":
                gmtTime = "GMT -08:00";
                break;
            case "America/Whitehorse":
                gmtTime = "GMT -08:00";
                break;
            case "America/Boise":
                gmtTime = "GMT -07:00";
                break;
            case "America/Cambridge_Bay":
                gmtTime = "GMT -07:00";
                break;
            case "America/Chihuahua":
                gmtTime = "GMT -07:00";
                break;
            case "America/Creston":
                gmtTime = "GMT -07:00";
                break;
            case "America/Dawson_Creek":
                gmtTime = "GMT -07:00";
                break;
            case "America/Edmonton":
                gmtTime = "GMT -07:00";
                break;
            case "America/Fort_Nelson":
                gmtTime = "GMT -07:00";
                break;
            case "America/Hermosillo":
                gmtTime = "GMT -07:00";
                break;
            case "America/Inuvik":
                gmtTime = "GMT -07:00";
                break;
            case "America/Mazatlan":
                gmtTime = "GMT -07:00";
                break;
            case "America/Ojinaga":
                gmtTime = "GMT -07:00";
                break;
            case "America/Shiprock":
                gmtTime = "GMT -07:00";
                break;
            case "America/Yellowknife":
                gmtTime = "GMT -07:00";
                break;
            default:
                // Handle unknown time zones
                gmtTime = "Unknown";
                break;
        }
        return gmtTime;
    }
}
