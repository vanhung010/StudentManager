package com.vhung.studentmanager.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class OtpStore {

    private final Map<String, OtpEntry> store = new ConcurrentHashMap<>();

    public void save(String username, String otpCode) {
        store.put(username, new OtpEntry(otpCode, LocalDateTime.now().plusMinutes(5)));
    }

    public OtpEntry get(String username) {
        return store.get(username);
    }

    public void remove(String username) {
        store.remove(username);
    }

    @Getter
    @AllArgsConstructor
    public static class OtpEntry  {
        private String otpCode;
        private LocalDateTime expiresAt;
    }

}
