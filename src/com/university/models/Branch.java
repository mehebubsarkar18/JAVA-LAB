package com.university.models;

import com.university.interfaces.Displayable;
import java.util.ArrayList;
import java.util.List;

/**
 * Branch class representing a branch in the university
 * Implements Displayable interface
 * Manages branch semesters and overall information
 */
public class Branch implements Displayable {
    private int branchId;
    private String branchName;
    private int totalCapacity;
    private List<Semester> semesters;
    
    /**
     * Constructor for Branch
     * @param branchId unique branch ID
     * @param branchName name of the branch
     * @param totalCapacity total capacity of the branch for 1st SEM
     */
    public Branch(int branchId, String branchName, int totalCapacity) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.totalCapacity = totalCapacity;
        this.semesters = new ArrayList<>();
        initializeSemesters();
    }
    
    /**
     * Initialize 8 semesters for the branch
     */
    private void initializeSemesters() {
        String[] semesterNames = {
            "1st SEM",
            "2nd SEM",
            "3rd SEM",
            "4th SEM",
            "5th SEM",
            "6th SEM",
            "7th SEM",
            "8th SEM"
        };
        int[] semesterCapacities = {
            totalCapacity,
            0,
            totalCapacity / 2,
            0,
            0,
            0,
            0,
            0
        };

        for (int i = 1; i <= 8; i++) {
            semesters.add(new Semester(i, semesterNames[i - 1], semesterCapacities[i - 1]));
        }
    }
    
    // Getter methods
    public int getBranchId() {
        return branchId;
    }
    
    public String getBranchName() {
        return branchName;
    }
    
    public int getTotalCapacity() {
        return totalCapacity;
    }
    
    public int getCurrentStudentCount() {
        return semesters.stream().mapToInt(Semester::getCurrentStudentCount).sum();
    }
    
    /**
     * Convenience method to get remaining seats for 1st SEM
     * @return number of remaining seats
     */
    public int getRemainingSeats() {
        Semester s1 = getSemesterByNumber(1);
        return s1 != null ? s1.getRemainingSeats() : 0;
    }
    
    public List<Semester> getSemesters() {
        return semesters;
    }
    
    public Semester getSemesterByNumber(int semesterNumber) {
        return semesters.stream()
                .filter(s -> s.getSemesterNumber() == semesterNumber)
                .findFirst()
                .orElse(null);
    }
    
    public Semester getSemesterByName(String semesterName) {
        return semesters.stream()
                .filter(s -> s.getSemesterName().equalsIgnoreCase(semesterName))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Setter for branch name
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    /**
     * Display branch information
     */
    @Override
    public void display() {
        System.out.println("Branch ID: " + branchId);
        System.out.println("Branch Name: " + branchName);
        Semester s1 = getSemesterByNumber(1);
        Semester s3 = getSemesterByNumber(3);
        if (s1 != null) {
            System.out.println("1st SEM Capacity: " + s1.getTotalCapacity());
            System.out.println("1st SEM Admitted: " + s1.getCurrentStudentCount());
            System.out.println("1st SEM Remaining Seats: " + s1.getRemainingSeats());
        }
        if (s3 != null) {
            System.out.println("3rd SEM Capacity: " + s3.getTotalCapacity());
            System.out.println("3rd SEM Admitted: " + s3.getCurrentStudentCount());
            System.out.println("3rd SEM Remaining Seats: " + s3.getRemainingSeats());
        }
    }
    
    /**
     * String representation of Branch
     */
    @Override
    public String toString() {
        Semester s1 = getSemesterByNumber(1);
        return branchName + (s1 != null ? " (1st SEM Free: " + s1.getRemainingSeats() + ")" : "");
    }
}
