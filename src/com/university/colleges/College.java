package com.university.colleges;

import com.university.models.Branch;
import com.university.interfaces.Displayable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract College class representing a college in the university
 * Follows Liskov Substitution Principle - can be substituted by its subclasses
 * Encapsulates college data and methods for branch management
 */
public abstract class College implements Displayable {
    protected int collegeId;
    protected String collegeName;
    protected List<Branch> branches;
    
    /**
     * Constructor for College
     * @param collegeId unique college ID
     * @param collegeName name of the college
     */
    public College(int collegeId, String collegeName) {
        this.collegeId = collegeId;
        this.collegeName = collegeName;
        this.branches = new ArrayList<>();
        initializeBranches();
    }
    
    /**
     * Abstract method to initialize branches
     * Each college must implement this to create its own branches
     */
    protected abstract void initializeBranches();
    
    // Getter methods
    public int getCollegeId() {
        return collegeId;
    }
    
    public String getCollegeName() {
        return collegeName;
    }
    
    public List<Branch> getBranches() {
        return branches;
    }
    
    /**
     * Get a specific branch by name
     * @param branchName the name of the branch
     * @return the Branch object or null if not found
     */
    public Branch getBranchByName(String branchName) {
        for (Branch branch : branches) {
            if (branch.getBranchName().equals(branchName)) {
                return branch;
            }
        }
        return null;
    }
    
    /**
     * Get all branches for display
     * @return list of branches
     */
    public List<Branch> getAllBranches() {
        return branches;
    }
    
    /**
     * Add a branch to the college
     * @param branch the branch to add
     */
    protected void addBranch(Branch branch) {
        branches.add(branch);
    }
    
    /**
     * Display college information
     */
    @Override
    public void display() {
        System.out.println("College ID: " + collegeId);
        System.out.println("College Name: " + collegeName);
        System.out.println("Branches: " + branches.size());
        for (Branch branch : branches) {
            System.out.println("  - " + branch.getBranchName());
        }
    }
    
    /**
     * String representation of College
     */
    @Override
    public String toString() {
        return collegeName;
    }
}
