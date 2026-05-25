package com.university.interfaces;

/**
 * Interface for capacity management
 * Follows Interface Segregation Principle
 */
public interface CapacityManageable {
    /**
     * Check if seats are available
     * @return true if seats are available, false otherwise
     */
    boolean isSeatsAvailable();
    
    /**
     * Get remaining seats
     * @return number of remaining seats
     */
    int getRemainingSeats();
    
    /**
     * Increase student count
     */
    void increaseStudentCount();

    /**
     * Decrease student count
     */
    void decreaseStudentCount();
}
