package com.university.interfaces;

/**
 * Interface for searchable objects
 * Follows Interface Segregation Principle
 */
public interface Searchable {
    /**
     * Search by ID
     * @param id the ID to search for
     * @return true if found, false otherwise
     */
    boolean searchById(int id);
}
