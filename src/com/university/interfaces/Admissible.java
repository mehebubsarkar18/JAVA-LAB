package com.university.interfaces;

/**
 * Interface for admissible objects
 * Follows Interface Segregation Principle
 */
public interface Admissible {
    /**
     * Check if admission is possible
     * @return true if admission can be done, false otherwise
     */
    boolean canAdmit();
    
    /**
     * Perform admission
     */
    void admit();
}
