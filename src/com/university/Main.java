package com.university;

import com.university.colleges.*;
import com.university.repositories.StudentRepositoryImpl;
import com.university.services.AdmissionService;
import com.university.ui.AdmissionPortalUI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main class - Entry point for the University Admission Portal
 * Initializes the application and handles the main program flow
 */
public class Main {
    /**
     * Main method - starts the application
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize colleges
        List<College> colleges = initializeColleges();
        
        // Initialize repository
        StudentRepositoryImpl studentRepository = new StudentRepositoryImpl();
        
        // Initialize services
        AdmissionService admissionService = new AdmissionService(studentRepository);
        
        // Sync capacities from loaded data
        admissionService.syncCapacities(colleges);
        
        // Use native look and feel when available
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Launch Swing UI
        SwingUtilities.invokeLater(() -> {
            AdmissionPortalUI ui = new AdmissionPortalUI(admissionService, colleges);
            ui.setVisible(true);
        });
    }
    
    /**
     * Initialize all colleges with their branches
     * @return list of initialized colleges
     */
    private static List<College> initializeColleges() {
        List<College> colleges = new ArrayList<>();
        colleges.add(new AECCollege());
        colleges.add(new JECCollege());
        colleges.add(new BVECCollege());
        colleges.add(new GECCollege());
        return colleges;
    }
}

