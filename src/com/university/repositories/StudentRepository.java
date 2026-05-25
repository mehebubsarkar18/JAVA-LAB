package com.university.repositories;

import com.university.models.Student;
import java.util.List;

/**
 * StudentRepository interface
 * Follows Dependency Inversion Principle
 * High-level services depend on this interface, not concrete implementation
 */
public interface StudentRepository {
    /**
     * Add a student to the repository
     * @param student the student to add
     * @return true if added successfully, false otherwise
     */
    boolean addStudent(Student student);
    
    /**
     * Find a student by ID
     * @param studentId the student ID to search for
     * @return the Student object if found, null otherwise
     */
    Student findStudentById(int studentId);
    
    /**
     * Get all students
     * @return list of all students
     */
    List<Student> getAllStudents();
    
    /**
     * Check if student ID already exists
     * @param studentId the student ID to check
     * @return true if exists, false otherwise
     */
    boolean studentExists(int studentId);
    
    /**
     * Delete a student from the repository
     * @param studentId the student ID to delete
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteStudent(int studentId);
    
    /**
     * Get total number of students
     * @return count of students
     */
    int getTotalStudents();
}
