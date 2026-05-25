package com.university.tests;

import com.university.colleges.AECCollege;
import com.university.colleges.College;
import com.university.models.Branch;
import com.university.models.Student;
import com.university.repositories.StudentRepositoryImpl;
import com.university.services.AdmissionService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdmissionTest {
    public static void main(String[] args) {
        testAdmissionLogic();
        testPersistence();
        System.out.println("All tests passed!");
    }

    private static void testAdmissionLogic() {
        System.out.println("Testing admission logic...");
        
        // Clean up before test
        File dataFile = new File("students.csv");
        if (dataFile.exists()) dataFile.delete();

        StudentRepositoryImpl repo = new StudentRepositoryImpl();
        AdmissionService service = new AdmissionService(repo);
        
        AECCollege college = new AECCollege();
        Branch branch = college.getBranches().get(0); // CSE
        
        // Admit a student
        Student s1 = new Student(101, "Test User", 20, "Male", college.getCollegeName(), branch.getBranchName(), "1st SEM");
        boolean result = service.processAdmission(s1, branch);
        
        if (!result) throw new RuntimeException("Admission failed for valid student");
        if (repo.getTotalStudents() != 1) throw new RuntimeException("Student count mismatch");
        if (branch.getSemesterByNumber(1).getCurrentStudentCount() != 1) throw new RuntimeException("Semester count mismatch");
        
        // Try duplicate ID
        Student s2 = new Student(101, "Duplicate", 21, "Female", college.getCollegeName(), branch.getBranchName(), "1st SEM");
        boolean result2 = service.processAdmission(s2, branch);
        if (result2) throw new RuntimeException("Admission succeeded for duplicate ID");
        
        System.out.println("Admission logic test passed.");
    }

    private static void testPersistence() {
        System.out.println("Testing persistence...");
        
        // Ensure data is saved from previous test or new one
        StudentRepositoryImpl repo1 = new StudentRepositoryImpl();
        AdmissionService service1 = new AdmissionService(repo1);
        AECCollege college = new AECCollege();
        Branch branch = college.getBranches().get(0);
        
        if (repo1.getTotalStudents() == 0) {
            service1.processAdmission(new Student(201, "Persist User", 22, "Other", college.getCollegeName(), branch.getBranchName(), "1st SEM"), branch);
        }
        
        int countBefore = repo1.getTotalStudents();
        
        // Load in a new repository instance
        StudentRepositoryImpl repo2 = new StudentRepositoryImpl();
        if (repo2.getTotalStudents() != countBefore) {
            throw new RuntimeException("Persistence failed: loaded count " + repo2.getTotalStudents() + " != " + countBefore);
        }
        
        // Sync capacities
        AdmissionService service2 = new AdmissionService(repo2);
        List<College> colleges = new ArrayList<>();
        colleges.add(college);
        service2.syncCapacities(colleges);
        
        if (college.getBranches().get(0).getSemesterByNumber(1).getCurrentStudentCount() == 0) {
            throw new RuntimeException("Capacity sync failed");
        }
        
        System.out.println("Persistence test passed.");
    }
}
