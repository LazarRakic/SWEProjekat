package com.example.projekatproba;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

public class TimeUtility {

    /**
     * Converts local date time to time in milliseconds (long)
     *
     * @param localDateTime
     * @return
     */
    public Long convertLocalDateTimeToLong(LocalDateTime localDateTime) {
        if(localDateTime!=null) {
            return localDateTime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        } else {
            return null;
        }
    }

    public LocalDateTime convertLongToLocalDateTime(Long timestamp)
    {
        if(timestamp != null) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
        } else {
            return null;
        }
    }
}
