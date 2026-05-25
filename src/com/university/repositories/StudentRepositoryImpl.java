package com.university.repositories;

import com.university.models.Student;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentRepositoryImpl class
 * Concrete implementation of StudentRepository interface
 * Manages student data using ArrayList and provides CSV persistence.
 */
public class StudentRepositoryImpl implements StudentRepository {
    private List<Student> students;
    private final String DATA_FILE = "students.csv";
    
    /**
     * Constructor for StudentRepositoryImpl
     */
    public StudentRepositoryImpl() {
        this.students = new ArrayList<>();
        loadStudents();
    }
    
    /**
     * Add a student to the repository
     * @param student the student to add
     * @return true if added successfully, false otherwise
     */
    @Override
    public boolean addStudent(Student student) {
        if (student != null && !studentExists(student.getStudentId())) {
            boolean added = students.add(student);
            if (added) {
                saveStudents();
            }
            return added;
        }
        return false;
    }
    
    /**
     * Find a student by ID
     * @param studentId the student ID to search for
     * @return the Student object if found, null otherwise
     */
    @Override
    public Student findStudentById(int studentId) {
        for (Student student : students) {
            if (student.searchById(studentId)) {
                return student;
            }
        }
        return null;
    }
    
    /**
     * Get all students
     * @return list of all students
     */
    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    
    /**
     * Check if student ID already exists
     * @param studentId the student ID to check
     * @return true if exists, false otherwise
     */
    @Override
    public boolean studentExists(int studentId) {
        return findStudentById(studentId) != null;
    }
    
    /**
     * Delete a student from the repository
     * @param studentId the student ID to delete
     * @return true if deleted successfully, false otherwise
     */
    @Override
    public boolean deleteStudent(int studentId) {
        Student toDelete = findStudentById(studentId);
        if (toDelete != null) {
            boolean removed = students.remove(toDelete);
            if (removed) {
                saveStudents();
            }
            return removed;
        }
        return false;
    }

    /**
     * Get total number of students
     * @return count of students
     */
    @Override
    public int getTotalStudents() {
        return students.size();
    }

    /**
     * Save students to CSV file
     */
    private void saveStudents() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Student s : students) {
                writer.println(String.format("%d,%s,%d,%s,%s,%s,%s,%d",
                    s.getStudentId(),
                    s.getName(),
                    s.getAge(),
                    s.getGender(),
                    s.getCollegeName(),
                    s.getBranchName(),
                    s.getSemester(),
                    s.getAdmissionNumber()
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving student data: " + e.getMessage());
        }
    }

    /**
     * Load students from CSV file
     */
    private void loadStudents() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int age = Integer.parseInt(parts[2]);
                    String gender = parts[3];
                    String college = parts[4];
                    String branch = parts[5];
                    String semester = parts[6];
                    int admissionNo = Integer.parseInt(parts[7]);

                    Student s = new Student(id, name, age, gender, college, branch, semester, admissionNo);
                    students.add(s);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading student data: " + e.getMessage());
        }
    }
}
