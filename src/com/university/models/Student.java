package com.university.models;

import com.university.interfaces.Displayable;
import com.university.interfaces.Searchable;

/**
 * Student class representing a student in the university
 * Implements Displayable and Searchable interfaces
 * Encapsulates all student-related data
 */
public class Student implements Displayable, Searchable {
    private int studentId;
    private String name;
    private int age;
    private String gender;
    private double hsPercentage;
    private String collegeName;
    private String branchName;
    private String semester;
    private static int admissionCounter = 1001; // Auto-generate admission number
    private int admissionNumber;
    
    /**
     * Update the admission counter if the provided value is higher.
     * Used when loading data from persistence.
     */
    public static void updateAdmissionCounter(int lastAdmissionNumber) {
        if (lastAdmissionNumber >= admissionCounter) {
            admissionCounter = lastAdmissionNumber + 1;
        }
    }
    
    /**
     * Constructor for Student with explicit admission number.
     * Used when loading data from persistence.
     */
    public Student(int studentId, String name, int age, String gender, 
                   String collegeName, String branchName, String semester, int admissionNumber) {
        this(studentId, name, age, gender, 0.0, collegeName, branchName, semester, admissionNumber);
    }

    public Student(int studentId, String name, int age, String gender, double hsPercentage,
                   String collegeName, String branchName, String semester, int admissionNumber) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hsPercentage = hsPercentage;
        this.collegeName = collegeName;
        this.branchName = branchName;
        this.semester = semester;
        this.admissionNumber = admissionNumber;
        updateAdmissionCounter(admissionNumber);
    }
    
    /**
     * Constructor for Student
     * @param studentId unique student ID
     * @param name student name
     * @param age student age
     * @param gender student gender
     * @param collegeName college name
     * @param branchName branch name
     * @param semester semester
     */
    public Student(int studentId, String name, int age, String gender, 
                   String collegeName, String branchName, String semester) {
        this(studentId, name, age, gender, 0.0, collegeName, branchName, semester);
    }

    public Student(int studentId, String name, int age, String gender, double hsPercentage,
                   String collegeName, String branchName, String semester) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hsPercentage = hsPercentage;
        this.collegeName = collegeName;
        this.branchName = branchName;
        this.semester = semester;
        this.admissionNumber = admissionCounter++;
    }
    
    // Getter methods
    public int getStudentId() {
        return studentId;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getGender() {
        return gender;
    }

    public double getHsPercentage() {
        return hsPercentage;
    }
    
    public String getCollegeName() {
        return collegeName;
    }
    
    public String getBranchName() {
        return branchName;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public int getAdmissionNumber() {
        return admissionNumber;
    }
    
    // Setter methods
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHsPercentage(double hsPercentage) {
        this.hsPercentage = hsPercentage;
    }
    
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    /**
     * Search student by ID
     * @param id the ID to search for
     * @return true if student ID matches
     */
    @Override
    public boolean searchById(int id) {
        return this.studentId == id;
    }
    
    /**
     * Display student information
     */
    @Override
    public void display() {
        System.out.println("======== Student Details ========");
        System.out.println("Admission Number: " + admissionNumber);
        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Gender: " + gender);
        if (hsPercentage > 0) {
            System.out.println("HS Percentage: " + hsPercentage);
        }
        System.out.println("College: " + collegeName);
        System.out.println("Branch: " + branchName);
        System.out.println("Semester: " + semester);
        System.out.println("==================================");
    }
    
    /**
     * String representation of Student
     */
    @Override
    public String toString() {
        return studentId + " | " + name + " | " + age + " | " + gender +
               " | " + hsPercentage +
               " | " + collegeName + " | " + branchName + " | " + semester;
    }
}
