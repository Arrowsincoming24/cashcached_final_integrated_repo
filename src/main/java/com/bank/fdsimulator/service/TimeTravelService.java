package com.bank.fdsimulator.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Time Travel Service for testing FD maturity and time-dependent features
 * Allows simulating future dates without changing system time
 */
@Service
public class TimeTravelService {
    
    private LocalDate simulatedDate = null;
    private boolean timeTravelEnabled = false;
    
    /**
     * Enable time travel and set simulated date
     */
    public void enableTimeTravel(LocalDate date) {
        this.simulatedDate = date;
        this.timeTravelEnabled = true;
        System.out.println("⏰ TIME TRAVEL ENABLED: Simulated date set to " + date);
    }
    
    /**
     * Disable time travel and return to current date
     */
    public void disableTimeTravel() {
        this.timeTravelEnabled = false;
        this.simulatedDate = null;
        System.out.println("⏰ TIME TRAVEL DISABLED: Using current date");
    }
    
    /**
     * Get current date (simulated or real)
     */
    public LocalDate getCurrentDate() {
        return timeTravelEnabled && simulatedDate != null ? simulatedDate : LocalDate.now();
    }
    
    /**
     * Get current date time (simulated or real)
     */
    public LocalDateTime getCurrentDateTime() {
        if (timeTravelEnabled && simulatedDate != null) {
            return simulatedDate.atStartOfDay();
        }
        return LocalDateTime.now();
    }
    
    /**
     * Fast forward time by specified days
     */
    public void fastForward(int days) {
        if (!timeTravelEnabled) {
            enableTimeTravel(LocalDate.now());
        }
        simulatedDate = simulatedDate.plusDays(days);
        System.out.println("⏰ FAST FORWARD: Advanced " + days + " days to " + simulatedDate);
    }
    
    /**
     * Rewind time by specified days
     */
    public void rewind(int days) {
        if (!timeTravelEnabled) {
            enableTimeTravel(LocalDate.now());
        }
        simulatedDate = simulatedDate.minusDays(days);
        System.out.println("⏰ REWIND: Went back " + days + " days to " + simulatedDate);
    }
    
    /**
     * Check if time travel is currently enabled
     */
    public boolean isTimeTravelEnabled() {
        return timeTravelEnabled;
    }
    
    /**
     * Get the simulated date (null if time travel disabled)
     */
    public LocalDate getSimulatedDate() {
        return simulatedDate;
    }
    
    /**
     * Jump to a specific date in the future
     */
    public void jumpToDate(LocalDate date) {
        enableTimeTravel(date);
    }
    
    /**
     * Calculate days between current (simulated) date and target date
     */
    public long daysUntil(LocalDate targetDate) {
        return getCurrentDate().until(targetDate).getDays();
    }
    
    /**
     * Check if a date is in the past (relative to simulated time)
     */
    public boolean isPast(LocalDate date) {
        return date.isBefore(getCurrentDate());
    }
    
    /**
     * Check if a date is in the future (relative to simulated time)
     */
    public boolean isFuture(LocalDate date) {
        return date.isAfter(getCurrentDate());
    }
}
