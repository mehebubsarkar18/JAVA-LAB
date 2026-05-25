package com.university.services;

import com.university.colleges.College;
import com.university.models.Branch;
import com.university.models.Semester;
import com.university.models.Student;
import com.university.repositories.StudentRepository;
import java.util.List;

/**
 * AdmissionService class
 * Handles all admission-related logic
 * Follows Single Responsibility Principle - only handles admission operations
 * Follows Dependency Inversion Principle - depends on StudentRepository interface
 */
public class AdmissionService {
    private StudentRepository studentRepository;
    
    /**
     * Constructor for AdmissionService
     * @param studentRepository the repository to use for storing students
     */
    public AdmissionService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    /**
     * Process student admission
     * @param student the student to admit
     * @param branch the branch to admit to
     * @return true if admission successful, false otherwise
     */
    public boolean processAdmission(Student student, Branch branch) {
        int semesterNumber = parseSemesterNumber(student.getSemester());
        Semester semester = branch.getSemesterByNumber(semesterNumber);

        if (semester == null || !semester.isSeatsAvailable()) {
            return false;
        }

        if (studentRepository.studentExists(student.getStudentId())) {
            return false;
        }

        if (studentRepository.addStudent(student)) {
            semester.increaseStudentCount();
            return true;
        }

        return false;
    }

    /**
     * Cancel admission and restore seat capacity
     * @param studentId the student ID to cancel
     * @param colleges list of colleges to find and update branch capacity
     * @return true if successful, false otherwise
     */
    public boolean cancelAdmission(int studentId, List<College> colleges) {
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) return false;

        if (studentRepository.deleteStudent(studentId)) {
            // Restore seat capacity
            for (College college : colleges) {
                if (college.getCollegeName().equalsIgnoreCase(student.getCollegeName())) {
                    for (Branch branch : college.getBranches()) {
                        if (branch.getBranchName().equalsIgnoreCase(student.getBranchName())) {
                            int semNum = parseSemesterNumber(student.getSemester());
                            Semester sem = branch.getSemesterByNumber(semNum);
                            if (sem != null) {
                                sem.decreaseStudentCount();
                                return true;
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Sync semester capacities
 based on students currently in the repository.
     * Useful when loading data from persistence.
     */
    public void syncCapacities(List<College> colleges) {
        for (Student student : studentRepository.getAllStudents()) {
            for (College college : colleges) {
                if (college.getCollegeName().equalsIgnoreCase(student.getCollegeName())) {
                    for (Branch branch : college.getBranches()) {
                        if (branch.getBranchName().equalsIgnoreCase(student.getBranchName())) {
                            int semNum = parseSemesterNumber(student.getSemester());
                            Semester sem = branch.getSemesterByNumber(semNum);
                            if (sem != null) {
                                sem.increaseStudentCount();
                            }
                        }
                    }
                }
            }
        }
    }

    public void displayBranchCapacity(College college) {
        System.out.println("\n========== Branch Capacity: " + college.getCollegeName() + " ==========");
        for (Branch branch : college.getBranches()) {
            branch.display();
            System.out.println("---");
        }
        System.out.println("==================================================\n");
    }

    public int parseSemesterNumber(String semester) {
        if (semester == null || semester.isEmpty()) {
            return -1;
        }
        try {
            // Extracts the first number found in the string
            String cleaned = semester.replaceAll("[^0-9]", " ").trim();
            if (cleaned.isEmpty()) return -1;
            return Integer.parseInt(cleaned.split("\\s+")[0]);
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * Get student by ID
     * @param studentId the student ID to search for
     * @return the Student object if found, null otherwise
     */
    public Student getStudentById(int studentId) {
        return studentRepository.findStudentById(studentId);
    }
    
    /**
     * Get all students
     * @return list of all students
     */
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }
    
    /**
     * Get total number of students
     * @return count of students
     */
    public int getTotalStudents() {
        return studentRepository.getTotalStudents();
    }
}
