package com.viktor.aaalife.setup.api.v1.models;

import java.util.List;
import java.util.Map;

public record InvalidBookingCase(
        String caseName,
        Map<String, Object> payload,
        List<Integer> expectedStatuses
        ) {
}