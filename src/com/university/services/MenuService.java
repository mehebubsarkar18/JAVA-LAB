package com.university.services;

import com.university.colleges.College;
import com.university.models.Branch;
import com.university.models.Semester;
import com.university.models.Student;
import java.util.List;
import java.util.Scanner;

/**
 * MenuService class
 * Handles all menu operations and user input
 * Follows Single Responsibility Principle - only handles menu and UI operations
 * Follows Dependency Inversion Principle - depends on AdmissionService interface
 */
public class MenuService {
    private AdmissionService admissionService;
    private List<College> colleges;
    private Scanner scanner;
    
    /**
     * Constructor for MenuService
     * @param admissionService the admission service to use
     * @param colleges the list of colleges
     */
    public MenuService(AdmissionService admissionService, List<College> colleges) {
        this.admissionService = admissionService;
        this.colleges = colleges;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Display main menu
     */
    public void displayMainMenu() {
        System.out.println("\n========================================");
        System.out.println("  Welcome to University Admission Portal");
        System.out.println("========================================");
        System.out.println("1. New Admission");
        System.out.println("2. View Student Details");
        System.out.println("3. View All Students");
        System.out.println("4. View Branch Capacity");
        System.out.println("5. Exit");
        System.out.println("========================================\n");
    }
    
    /**
     * Handle new admission
     */
    public void handleNewAdmission() {
        try {
            System.out.println("\n========== New Admission ==========");
            
            // Validate and get student ID
            System.out.print("Enter Student ID: ");
            int studentId = getValidIntInput();
            if (studentId == -1) return;
            
            if (admissionService.getStudentById(studentId) != null) {
                System.out.println("Error: Student ID already exists!");
                return;
            }
            
            // Get student name
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Error: Name cannot be empty!");
                return;
            }
            
            // Validate and get age
            System.out.print("Enter Age (15-45): ");
            int age = getValidIntInput();
            if (age < 15 || age > 45) {
                System.out.println("Error: Age must be between 15 and 45!");
                return;
            }
            
            // Get gender
            System.out.print("Enter Gender: ");
            String gender = scanner.nextLine().trim();
            if (gender.isEmpty()) {
                System.out.println("Error: Gender cannot be empty!");
                return;
            }
            
            // Select college
            College selectedCollege = selectCollege();
            if (selectedCollege == null) {
                return;
            }
            
            // Select branch
            Branch selectedBranch = selectBranch(selectedCollege);
            if (selectedBranch == null) {
                return;
            }
            
            // Select semester
            String selectedSemester = selectSemester();
            if (selectedSemester == null) {
                return;
            }
            
            // Create student object
            Student student = new Student(studentId, name, age, gender,
                    selectedCollege.getCollegeName(), selectedBranch.getBranchName(),
                    selectedSemester);
            
            // Process admission
            if (admissionService.processAdmission(student, selectedBranch)) {
                Semester selectedSemesterObj = selectedBranch.getSemesterByNumber(admissionService.parseSemesterNumber(selectedSemester));
                System.out.println("\n========== Admission Successful! ==========");
                student.display();
                if (selectedSemesterObj != null) {
                    System.out.println("Remaining Seats in " + selectedSemesterObj.getSemesterName() + ": " +
                            selectedSemesterObj.getRemainingSeats() + " / " + selectedSemesterObj.getTotalCapacity());
                }
                System.out.println("==========================================\n");
            } else {
                System.out.println("\n========== Admission Failed! ==========");
                System.out.println("Selected semester may be full or the student ID already exists.");
                System.out.println("========================================\n");
            }
        } catch (Exception e) {
            System.out.println("Error during admission: " + e.getMessage());
            scanner.nextLine(); // Clear buffer
        }
    }
    
    /**
     * Handle view student details
     */
    public void handleViewStudentDetails() {
        try {
            System.out.print("\nEnter Student ID to search: ");
            int studentId = getValidIntInput();
            if (studentId == -1) return;
            
            Student student = admissionService.getStudentById(studentId);
            if (student != null) {
                student.display();
            } else {
                System.out.println("Error: Student not found!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    /**
     * Handle view all students
     */
    public void handleViewAllStudents() {
        List<Student> students = admissionService.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("\nNo students admitted yet!");
            return;
        }
        
        System.out.println("\n========== All Admitted Students ==========");
        System.out.println(String.format("%-8s | %-15s | %-5s | %-8s | %-10s | %-30s | %-15s",
                "ID", "Name", "Age", "Gender", "College", "Branch", "Semester"));
        System.out.println("-------------------------------------------");
        
        for (Student student : students) {
            System.out.println(String.format("%-8d | %-15s | %-5d | %-8s | %-10s | %-30s | %-15s",
                    student.getStudentId(),
                    student.getName(),
                    student.getAge(),
                    student.getGender(),
                    student.getCollegeName().substring(0, Math.min(10, student.getCollegeName().length())),
                    student.getBranchName(),
                    student.getSemester()));
        }
        System.out.println("==========================================\n");
    }
    
    /**
     * Handle view branch capacity
     */
    public void handleViewBranchCapacity() {
        College selectedCollege = selectCollege();
        if (selectedCollege != null) {
            admissionService.displayBranchCapacity(selectedCollege);
        }
    }
    
    /**
     * Select a college
     * @return the selected College object or null if invalid
     */
    private College selectCollege() {
        System.out.println("\nSelect College:");
        for (int i = 0; i < colleges.size(); i++) {
            System.out.println((i + 1) + ". " + colleges.get(i).getCollegeName());
        }
        
        System.out.print("Enter Choice: ");
        int choice = getValidIntInput();
        
        if (choice >= 1 && choice <= colleges.size()) {
            return colleges.get(choice - 1);
        } else {
            System.out.println("Error: Invalid choice!");
            return null;
        }
    }
    
    /**
     * Select a branch
     * @param college the college to select branch from
     * @return the selected Branch object or null if invalid
     */
    private Branch selectBranch(College college) {
        System.out.println("\nSelect Branch:");
        List<Branch> branches = college.getBranches();
        
        for (int i = 0; i < branches.size(); i++) {
            Branch branch = branches.get(i);
            System.out.println((i + 1) + ". " + branch.getBranchName() + 
                             " (Available Seats: " + branch.getRemainingSeats() + ")");
        }
        
        System.out.print("Enter Choice: ");
        int choice = getValidIntInput();
        
        if (choice >= 1 && choice <= branches.size()) {
            return branches.get(choice - 1);
        } else {
            System.out.println("Error: Invalid choice!");
            return null;
        }
    }
    
    /**
     * Select a semester
     * @return the selected semester name or null if invalid
     */
    private String selectSemester() {
        System.out.println("\nSelect Semester:");
        String[] semesters = {
            "1st SEM",
            "3rd SEM (Lateral Entry)"
        };
        
        for (int i = 0; i < semesters.length; i++) {
            System.out.println((i + 1) + ". " + semesters[i]);
        }
        
        System.out.print("Enter Choice: ");
        int choice = getValidIntInput();
        
        if (choice >= 1 && choice <= semesters.length) {
            return semesters[choice - 1];
        } else {
            System.out.println("Error: Invalid choice!");
            return null;
        }
    }
    
    /**
     * Get valid integer input from user
     * @return the valid integer input
     */
    private int getValidIntInput() {
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return -1;
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number!");
            return -1;
        }
    }
    
    /**
     * Get user menu choice
     * @return the menu choice
     */
    public int getUserChoice() {
        System.out.print("Enter Choice: ");
        return getValidIntInput();
    }
    
    /**
     * Close scanner
     */
    public void closeScanner() {
        scanner.close();
    }
}
