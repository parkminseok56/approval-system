package com.example.approval.global.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class DateTimeUtil {

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    private DateTimeUtil() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE_ID);
    }
}
