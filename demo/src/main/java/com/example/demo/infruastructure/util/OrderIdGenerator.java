package com.example.demo.infruastructure.util;

import cn.hutool.json.JSONUtil;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderIdGenerator {

    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int LENGTH = 16;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private static final int MAX_COUNTER = 99999; // Adjust based on your needs

    // Unique machine identifier (e.g., last 4 digits of IP address or a unique machine ID)
    private static final int MACHINE_ID = RANDOM.nextInt(36);

    public static void main(String[] args) {
        Set<String> duplicateCodes = new HashSet<>();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 9999999; i++) {
            String code = generateOrderId();
            System.out.println(code);
            if (map.containsKey(code)) {
                map.put(code, map.get(code) + 1);
                duplicateCodes.add(code);
            } else {
                map.put(code, 1);
            }
        }
        System.out.println("Duplicate codes: " + JSONUtil.toJsonStr(duplicateCodes));
    }

    public static synchronized String generateOrderId() {
        StringBuilder orderId = new StringBuilder(LENGTH);

        // Add a timestamp component
        long timestamp = Instant.now().toEpochMilli();
        String timestampStr = Long.toString(timestamp, 36); // Convert to base36 to shorten
        orderId.append(timestampStr);

        // Add a machine identifier component
        orderId.append(CHARACTERS.charAt(MACHINE_ID));

        // Add a counter component
        int count = COUNTER.getAndIncrement();
        if (count > MAX_COUNTER) {
            COUNTER.set(0);
            count = COUNTER.getAndIncrement();
        }
        String countStr = String.format("%05d", count); // Pad with leading zeros
        orderId.append(countStr);

        // Fill the rest with random characters if needed
        while (orderId.length() < LENGTH) {
            orderId.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return orderId.toString();
    }
}
