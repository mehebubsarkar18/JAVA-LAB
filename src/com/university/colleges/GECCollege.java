package com.university.colleges;

import com.university.models.Branch;

/**
 * GECCollege class representing Golaghat Engineering College
 * Concrete implementation of abstract College class
 * Follows Open/Closed Principle - open for extension, closed for modification
 */
public class GECCollege extends College {
    /**
     * Constructor for GECCollege
     */
    public GECCollege() {
        super(4, "GEC - Golaghat Engineering College");
    }
    
    /**
     * Initialize branches for GEC
     */
    @Override
    protected void initializeBranches() {
        addBranch(new Branch(401, "Computer Science Engineering (CSE)", 60));
        addBranch(new Branch(402, "Electronics and Communication Engineering (ECE)", 60));
        addBranch(new Branch(403, "Mechanical Engineering (ME)", 45));
        addBranch(new Branch(404, "Civil Engineering (CE)", 45));
    }
}
