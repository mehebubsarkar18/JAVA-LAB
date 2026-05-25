package com.university.colleges;

import com.university.models.Branch;

/**
 * BVECCollege class representing Bineswar Brahma Engineering College
 * Concrete implementation of abstract College class
 * Follows Open/Closed Principle - open for extension, closed for modification
 */
public class BVECCollege extends College {
    /**
     * Constructor for BVECCollege
     */
    public BVECCollege() {
        super(3, "BVEC - Barak Valley Engineering College");
    }
    
    /**
     * Initialize branches for BVEC
     */
    @Override
    protected void initializeBranches() {
        addBranch(new Branch(301, "Computer Science Engineering (CSE)", 60));
        addBranch(new Branch(302, "Electronics and Communication Engineering (ECE)", 60));
        addBranch(new Branch(303, "Mechanical Engineering (ME)", 45));
        addBranch(new Branch(304, "Civil Engineering (CE)", 45));
    }
}
