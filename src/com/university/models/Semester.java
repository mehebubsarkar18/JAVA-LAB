package com.university.models;

import com.university.interfaces.CapacityManageable;
import com.university.interfaces.Displayable;

/**
 * Semester class representing an academic semester
 * Encapsulates semester information and capacity data
 * Implements CapacityManageable and Displayable interfaces
 */
public class Semester implements CapacityManageable, Displayable {
    private int semesterNumber;
    private String semesterName;
    private int totalCapacity;
    private int currentStudentCount;
    
    /**
     * Constructor for Semester
     * @param semesterNumber the semester number
     * @param semesterName the name of the semester
     * @param totalCapacity the capacity for this semester
     */
    public Semester(int semesterNumber, String semesterName, int totalCapacity) {
        this.semesterNumber = semesterNumber;
        this.semesterName = semesterName;
        this.totalCapacity = totalCapacity;
        this.currentStudentCount = 0;
    }
    
    // Getter methods
    public int getSemesterNumber() {
        return semesterNumber;
    }
    
    public String getSemesterName() {
        return semesterName;
    }
    
    public int getTotalCapacity() {
        return totalCapacity;
    }
    
    public int getCurrentStudentCount() {
        return currentStudentCount;
    }
    
    @Override
    public int getRemainingSeats() {
        return totalCapacity - currentStudentCount;
    }
    
    @Override
    public boolean isSeatsAvailable() {
        return currentStudentCount < totalCapacity;
    }
    
    @Override
    public void increaseStudentCount() {
        if (isSeatsAvailable()) {
            currentStudentCount++;
        }
    }
    
    @Override
    public void decreaseStudentCount() {
        if (currentStudentCount > 0) {
            currentStudentCount--;
        }
    }
    
    // Setter methods
    public void setSemesterNumber(int semesterNumber) {
        this.semesterNumber = semesterNumber;
    }
    
    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }
    
    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }
    
    public void setCurrentStudentCount(int currentStudentCount) {
        this.currentStudentCount = currentStudentCount;
    }
    
    /**
     * Display semester information
     */
    @Override
    public void display() {
        System.out.println("Semester " + semesterNumber + ": " + semesterName +
                " (" + currentStudentCount + "/" + totalCapacity + " seats)");
    }
    
    /**
     * String representation of Semester
     */
    @Override
    public String toString() {
        return semesterName;
    }
}
