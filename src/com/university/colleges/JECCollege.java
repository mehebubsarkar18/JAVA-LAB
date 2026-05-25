package com.university.colleges;

import com.university.models.Branch;

/**
 * JECCollege class representing Jorhat Engineering College
 * Concrete implementation of abstract College class
 * Follows Open/Closed Principle - open for extension, closed for modification
 */
public class JECCollege extends College {
    /**
     * Constructor for JECCollege
     */
    public JECCollege() {
        super(2, "JEC - Jorhat Engineering College");
    }
    
    /**
     * Initialize branches for JEC
     */
    @Override
    protected void initializeBranches() {
        addBranch(new Branch(201, "Computer Science Engineering (CSE)", 60));
        addBranch(new Branch(202, "Electronics and Communication Engineering (ECE)", 60));
        addBranch(new Branch(203, "Mechanical Engineering (ME)", 45));
        addBranch(new Branch(204, "Civil Engineering (CE)", 45));
    }
}
