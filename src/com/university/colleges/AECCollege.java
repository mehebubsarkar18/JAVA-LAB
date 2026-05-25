package com.university.colleges;

import com.university.models.Branch;

/**
 * AECCollege class representing Assam Engineering College
 * Concrete implementation of abstract College class
 * Follows Open/Closed Principle - open for extension, closed for modification
 */
public class AECCollege extends College {
    /**
     * Constructor for AECCollege
     */
    public AECCollege() {
        super(1, "AEC - Assam Engineering College");
    }
    
    /**
     * Initialize branches for AEC
     */
    @Override
    protected void initializeBranches() {
        addBranch(new Branch(101, "Computer Science Engineering (CSE)", 60));
        addBranch(new Branch(102, "Electronics and Communication Engineering (ECE)", 60));
        addBranch(new Branch(103, "Mechanical Engineering (ME)", 45));
        addBranch(new Branch(104, "Civil Engineering (CE)", 45));
    }
}
