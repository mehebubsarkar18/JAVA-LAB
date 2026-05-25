package com.university.ui;

import com.university.colleges.College;
import com.university.models.Branch;
import com.university.models.Semester;
import com.university.models.Student;
import com.university.services.AdmissionService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Modern Graphical User Interface for the University Admission Portal.
 * Implements a clean, responsive design with rounded components and intuitive navigation.
 */
public class AdmissionPortalUI extends JFrame {
    private final AdmissionService admissionService;
    private final List<College> colleges;
    private JTabbedPane mainTabs;

    // Form fields
    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField hsPercentageField;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> collegeComboBox;
    private JComboBox<String> branchComboBox;
    private JComboBox<String> semesterComboBox;
    private JTextArea admissionResultArea;
    private JLabel branchInfoLabel;
    private CardLayout admissionStepLayout;
    private JPanel admissionStepPanel;
    private JLabel admissionStepTitleLabel;
    private JLabel admissionSelectionLabel;
    private DefaultTableModel admissionBranchTableModel;
    private JTable admissionBranchTable;
    private JScrollPane admissionResultScrollPane;

    // Search fields
    private JTextField searchIdField;
    private JTextArea studentDetailsArea;

    // Student table fields
    private DefaultTableModel studentsTableModel;
    private TableRowSorter<DefaultTableModel> studentSorter;
    private JTextField studentsFilterField;
    private JLabel studentCountLabel;
    private JTable studentsTable;

    // Capacity table fields
    private JComboBox<String> capacityCollegeComboBox;
    private DefaultTableModel capacityTableModel;
    private JLabel capacitySummaryLabel;

    // Theme Colors
    private final Color PRIMARY_COLOR = new Color(0, 120, 215);
    private final Color ACCENT_COLOR = new Color(0, 180, 255);
    private final Color HOVER_COLOR = new Color(0, 220, 255);
    private final Color BACKGROUND_COLOR = new Color(245, 250, 255);
    private final Color TEXT_COLOR = new Color(50, 50, 50);
    private final Color SUCCESS_COLOR = new Color(0, 150, 80);
    private final Color ERROR_COLOR = new Color(220, 50, 50);

    public AdmissionPortalUI(AdmissionService admissionService, List<College> colleges) {
        this.admissionService = admissionService;
        this.colleges = colleges;
        initializeUI();
    }

    private void initializeUI() {
        applyGlobalStyles();
        setTitle("University Admission Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        // Switch to SCROLL_TAB_LAYOUT to prevent tabs from wrapping or 'hiding' each other
        mainTabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        styleTabbedPane(mainTabs);

        mainTabs.addTab("Dashboard", createDashboardPanel());
        mainTabs.addTab("New Admission", createAdmissionPanel());
        mainTabs.addTab("All Students", createAllStudentsPanel());
        mainTabs.addTab("View Student", createViewStudentPanel());
        mainTabs.addTab("Branch Capacity", createBranchCapacityPanel());
        mainTabs.setSelectedIndex(0);

        add(mainTabs);
        
        // Initial data load
        refreshStudentsTable();
        handleBranchCapacity();
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(new EmptyBorder(35, 35, 35, 35));

        JLabel titleLabel = new JLabel("University Admission Portal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(PRIMARY_COLOR);

        JLabel subtitleLabel = new JLabel("Choose an option to continue", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        subtitleLabel.setForeground(TEXT_COLOR);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        JPanel optionPanel = new JPanel(new GridLayout(2, 2, 22, 22));
        optionPanel.setOpaque(false);
        optionPanel.setBorder(new EmptyBorder(30, 120, 30, 120));

        optionPanel.add(createDashboardOption("New Admission", "Register a new student", 1));
        optionPanel.add(createDashboardOption("All Students", "View and manage admitted students", 2));
        optionPanel.add(createDashboardOption("View Student", "Search one student by ID", 3));
        optionPanel.add(createDashboardOption("Branch Capacity", "Check available seats", 4));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(optionPanel, BorderLayout.CENTER);
        return panel;
    }

    private JButton createDashboardOption(String title, String description, int tabIndex) {
        JButton button = new JButton("<html><center><b>" + title + "</b><br><span style='font-size:11px'>" + description + "</span></center></html>");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(25, 25, 25, 25));
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 18, 18);
                g2.setColor(new Color(210, 225, 245));
                g2.drawRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, 18, 18);
                super.paint(g2, c);
                g2.dispose();
            }
        });
        button.addActionListener(e -> mainTabs.setSelectedIndex(tabIndex));
        return button;
    }

    private void applyGlobalStyles() {
        Font uiFont = new Font("Segoe UI", Font.PLAIN, 15);
        UIManager.put("Label.font", uiFont);
        UIManager.put("Button.font", uiFont.deriveFont(Font.BOLD, 15f));
        UIManager.put("TextField.font", uiFont);
        UIManager.put("ComboBox.font", uiFont);
        UIManager.put("TextArea.font", new Font("Consolas", Font.PLAIN, 14));
        UIManager.put("Table.font", uiFont);
        UIManager.put("TableHeader.font", uiFont.deriveFont(Font.BOLD, 15f));
        
        // Larger Tab Font
        UIManager.put("TabbedPane.font", new Font("Segoe UI", Font.BOLD, 18));
        
        // Set basic colors
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("TabbedPane.background", new Color(230, 240, 255));
    }

    private void styleTabbedPane(JTabbedPane tabs) {
        tabs.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                // Set consistent padding for ALL tabs to prevent jumping
                tabInsets = new Insets(10, 40, 10, 40);
                selectedTabPadInsets = new Insets(2, 5, 2, 5); // Minimal extra padding for selected
                tabAreaInsets = new Insets(10, 10, 0, 10);
            }

            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isSelected) {
                    g2.setColor(ACCENT_COLOR);
                } else {
                    g2.setColor(new Color(210, 225, 245));
                }
                
                // Draw rounded tab background
                g2.fillRoundRect(x + 2, y + 2, w - 4, h - 1, 15, 15);
                
                // Add a small highlight on top for selected tab
                if (isSelected) {
                    g2.setColor(new Color(255, 255, 255, 50));
                    g2.fillRoundRect(x + 5, y + 5, w - 10, 5, 10, 10);
                }
                g2.dispose();
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                // Remove the default border around content
            }

            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
                // Remove the dashed focus rectangle
            }

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
                g.setFont(font);
                if (isSelected) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(TEXT_COLOR);
                }
                g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            }
        });
    }

    private void styleButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(ACCENT_COLOR);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);
                super.paint(g2, c);
                g2.dispose();
            }
        });
    }

    private JPanel createAdmissionPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        collegeComboBox = new JComboBox<>(getCollegeNames());
        branchComboBox = new JComboBox<>();
        semesterComboBox = new JComboBox<>(getSemesterNames());
        idField = new JTextField(15);
        nameField = new JTextField(20);
        ageField = new JTextField(15);
        hsPercentageField = new JTextField(15);
        genderComboBox = new JComboBox<>(new String[]{"Select Gender", "Male", "Female", "Other"});
        branchInfoLabel = new JLabel(" ");
        branchInfoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        collegeComboBox.addActionListener(e -> {
            updateBranchCombo();
            updateAdmissionBranchTable();
        });
        branchComboBox.addActionListener(e -> updateBranchInfo());
        semesterComboBox.addActionListener(e -> {
            updateBranchInfo();
            updateAdmissionBranchTable();
        });
        updateBranchCombo();

        admissionStepTitleLabel = new JLabel("Step 1: Select College", SwingConstants.CENTER);
        admissionStepTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        admissionStepTitleLabel.setForeground(PRIMARY_COLOR);

        admissionSelectionLabel = new JLabel(" ", SwingConstants.CENTER);
        admissionSelectionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        admissionSelectionLabel.setForeground(TEXT_COLOR);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setOpaque(false);
        headerPanel.add(admissionStepTitleLabel);
        headerPanel.add(admissionSelectionLabel);

        admissionStepLayout = new CardLayout();
        admissionStepPanel = new JPanel(admissionStepLayout);
        admissionStepPanel.setOpaque(false);
        admissionStepPanel.add(createCollegeSelectionStep(), "college");
        admissionStepPanel.add(createSemesterSelectionStep(), "semester");
        admissionStepPanel.add(createBranchSelectionStep(), "branch");
        admissionStepPanel.add(createStudentDetailsStep(), "details");

        admissionResultArea = new JTextArea(6, 0);
        admissionResultArea.setEditable(false);
        admissionResultArea.setBackground(new Color(252, 252, 252));
        admissionResultScrollPane = new JScrollPane(admissionResultArea);
        admissionResultScrollPane.setBorder(BorderFactory.createTitledBorder("Status & Log"));
        admissionResultScrollPane.setVisible(false);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(admissionStepPanel, BorderLayout.CENTER);
        mainPanel.add(admissionResultScrollPane, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createCollegeSelectionStep() {
        JPanel stepPanel = createWizardCard();
        stepPanel.add(new JLabel("Select College:"), createGbc(0, 0, 0.2));
        stepPanel.add(collegeComboBox, createGbc(1, 0, 0.8));

        JButton nextButton = new JButton("Next");
        styleButton(nextButton);
        nextButton.addActionListener(e -> {
            updateBranchCombo();
            showAdmissionStep("semester", "Step 2: Select Semester", getSelectedCollegeName());
        });
        stepPanel.add(nextButton, createButtonGbc(1));
        return wrapWizardCard(stepPanel);
    }

    private JPanel createBranchSelectionStep() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 60, 20, 60));

        admissionBranchTableModel = new DefaultTableModel(
                new String[]{"Branch", "Free Seats", "Total Capacity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        admissionBranchTable = new JTable(admissionBranchTableModel);
        styleTable(admissionBranchTable);
        panel.add(new JScrollPane(admissionBranchTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        JButton nextButton = new JButton("Next");
        styleButton(backButton);
        styleButton(nextButton);
        bottomPanel.add(new JLabel("Choose Branch:"));
        bottomPanel.add(branchComboBox);
        bottomPanel.add(branchInfoLabel);
        bottomPanel.add(backButton);
        bottomPanel.add(nextButton);

        backButton.addActionListener(e -> showAdmissionStep("semester", "Step 2: Select Semester", getSelectedCollegeName()));
        nextButton.addActionListener(e -> showAdmissionStep("details", "Step 4: Student Details",
                getSelectedCollegeName() + " | " + semesterComboBox.getSelectedItem() + " | " + getSelectedBranchName()));

        panel.add(bottomPanel, BorderLayout.SOUTH);
        updateAdmissionBranchTable();
        return panel;
    }

    private JPanel createSemesterSelectionStep() {
        JPanel stepPanel = createWizardCard();
        stepPanel.add(new JLabel("Admission Semester:"), createGbc(0, 0, 0.2));
        stepPanel.add(semesterComboBox, createGbc(1, 0, 0.8));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttons.setOpaque(false);
        JButton backButton = new JButton("Back");
        JButton nextButton = new JButton("Next");
        styleButton(backButton);
        styleButton(nextButton);
        buttons.add(backButton);
        buttons.add(nextButton);

        backButton.addActionListener(e -> showAdmissionStep("college", "Step 1: Select College", " "));
        nextButton.addActionListener(e -> {
            updateAdmissionBranchTable();
            showAdmissionStep("branch", "Step 3: Select Branch",
                    getSelectedCollegeName() + " | " + semesterComboBox.getSelectedItem());
        });

        GridBagConstraints gbc = createButtonGbc(1);
        stepPanel.add(buttons, gbc);
        return wrapWizardCard(stepPanel);
    }

    private JPanel createStudentDetailsStep() {
        JPanel formPanel = createWizardCard();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addFormField(formPanel, "Student ID:", idField, gbc, row++);
        addFormField(formPanel, "Full Name:", nameField, gbc, row++);
        addFormField(formPanel, "Age:", ageField, gbc, row++);
        addFormField(formPanel, "Gender:", genderComboBox, gbc, row++);
        addFormField(formPanel, "HS Percentage:", hsPercentageField, gbc, row++);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        actionPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        JButton admitButton = new JButton("Admit Student");
        JButton clearButton = new JButton("Clear Form");
        styleButton(backButton);
        styleButton(admitButton);
        styleButton(clearButton);
        backButton.addActionListener(e -> showAdmissionStep("branch", "Step 3: Select Branch",
                getSelectedCollegeName() + " | " + semesterComboBox.getSelectedItem()));
        admitButton.addActionListener(this::handleAdmission);
        clearButton.addActionListener(e -> clearAdmissionForm());
        actionPanel.add(backButton);
        actionPanel.add(admitButton);
        actionPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(actionPanel, gbc);

        return wrapWizardCard(formPanel);
    }

    private JPanel createWizardCard() {
        JPanel formPanel = new RoundedPanel(new GridBagLayout(), Color.WHITE, new Color(220, 230, 240), 16);
        formPanel.setPreferredSize(new Dimension(620, 380));
        formPanel.setBorder(new EmptyBorder(30, 35, 30, 35));
        return formPanel;
    }

    private JPanel wrapWizardCard(JPanel card) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);
        return wrapper;
    }

    private GridBagConstraints createGbc(int x, int y, double weightx) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = weightx;
        return gbc;
    }

    private GridBagConstraints createButtonGbc(int row) {
        GridBagConstraints gbc = createGbc(0, row, 1.0);
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        return gbc;
    }

    private void showAdmissionStep(String cardName, String title, String selection) {
        admissionStepTitleLabel.setText(title);
        admissionSelectionLabel.setText(selection);
        admissionStepLayout.show(admissionStepPanel, cardName);
        if (admissionResultScrollPane != null) {
            admissionResultScrollPane.setVisible("details".equals(cardName));
            admissionResultScrollPane.getParent().revalidate();
        }
        updateBranchInfo();
    }

    private void addFormField(JPanel panel, String label, JComponent component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        panel.add(component, gbc);
    }

    private JPanel createViewStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        searchPanel.setOpaque(false);
        searchIdField = new JTextField(15);
        JButton searchButton = new JButton("Find Student");
        JButton clearButton = new JButton("Reset");
        styleButton(searchButton);
        styleButton(clearButton);
        searchButton.setPreferredSize(new Dimension(140, 35));
        clearButton.setPreferredSize(new Dimension(100, 35));
        
        searchButton.addActionListener(e -> handleViewStudent());
        clearButton.addActionListener(e -> {
            searchIdField.setText("");
            studentDetailsArea.setText("");
        });

        searchPanel.add(new JLabel("Enter Student ID:"));
        searchPanel.add(searchIdField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        studentDetailsArea = new JTextArea();
        studentDetailsArea.setEditable(false);
        studentDetailsArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        studentDetailsArea.setMargin(new Insets(20, 20, 20, 20));
        
        JScrollPane scroll = new JScrollPane(studentDetailsArea);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAllStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel controlPanel = new JPanel(new BorderLayout(10, 0));
        controlPanel.setOpaque(false);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);
        studentsFilterField = new JTextField(20);
        JButton filterButton = new JButton("Filter");
        JButton refreshButton = new JButton("Refresh Data");
        styleButton(filterButton);
        styleButton(refreshButton);
        filterButton.setPreferredSize(new Dimension(100, 35));
        refreshButton.setPreferredSize(new Dimension(140, 35));

        filterButton.addActionListener(e -> filterStudentsTable());
        refreshButton.addActionListener(e -> refreshStudentsTable());

        filterPanel.add(new JLabel("Search Students:"));
        filterPanel.add(studentsFilterField);
        filterPanel.add(filterButton);
        filterPanel.add(refreshButton);

        studentCountLabel = new JLabel("Total Students: 0");
        studentCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentCountLabel.setForeground(PRIMARY_COLOR);

        controlPanel.add(filterPanel, BorderLayout.WEST);
        controlPanel.add(studentCountLabel, BorderLayout.EAST);

        String[] columns = {"ID", "Name", "Age", "Gender", "HS %", "College", "Branch", "Semester", "Admn No.", "Action"};
        studentsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return column == 9; // Only Action column is editable for the button
            }
        };
        studentsTable = new JTable(studentsTableModel);
        styleTable(studentsTable);
        
        // Setup Delete Button in Table
        setupTableAction(studentsTable);

        studentSorter = new TableRowSorter<>(studentsTableModel);
        studentsTable.setRowSorter(studentSorter);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(studentsTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBranchCapacityPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        selectPanel.setOpaque(false);
        capacityCollegeComboBox = new JComboBox<>(getCollegeNames());
        JButton showButton = new JButton("View Capacity");
        styleButton(showButton);
        showButton.setPreferredSize(new Dimension(150, 35));
        showButton.addActionListener(e -> handleBranchCapacity());

        selectPanel.add(new JLabel("Select College:"));
        selectPanel.add(capacityCollegeComboBox);
        selectPanel.add(showButton);

        capacitySummaryLabel = new JLabel(" ");
        capacitySummaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        capacitySummaryLabel.setForeground(SUCCESS_COLOR);

        topPanel.add(selectPanel, BorderLayout.WEST);
        topPanel.add(capacitySummaryLabel, BorderLayout.EAST);

        String[] cols = {"Branch Name", "1st SEM Capacity", "1st SEM Used", "1st SEM Free", "3rd SEM Capacity", "3rd SEM Used", "3rd SEM Free"};
        capacityTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable capacityTable = new JTable(capacityTableModel);
        styleTable(capacityTable);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(capacityTable), BorderLayout.CENTER);

        return panel;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setSelectionBackground(new Color(230, 245, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(false);
        table.setFillsViewportHeight(true);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setPreferredSize(new Dimension(0, 40));
        
        // Center-align the headers
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Center-align the cell content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // --- Action Handlers ---

    private void handleAdmission(ActionEvent event) {
        admissionResultArea.setText("");
        
        Optional<String> error = validateInputs();
        if (error.isPresent()) {
            showResult(error.get(), ERROR_COLOR);
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            double hsPercentage = Double.parseDouble(hsPercentageField.getText().trim());
            String gender = (String) genderComboBox.getSelectedItem();
            
            int collegeIdx = collegeComboBox.getSelectedIndex();
            College college = colleges.get(collegeIdx);
            
            int branchIdx = branchComboBox.getSelectedIndex();
            Branch branch = college.getBranches().get(branchIdx);
            
            String semester = (String) semesterComboBox.getSelectedItem();

            Student student = new Student(id, name, age, gender, hsPercentage, college.getCollegeName(), branch.getBranchName(), semester);
            
            boolean success = admissionService.processAdmission(student, branch);
            if (success) {
                String successMessage = "Admission Successful!\n\n" +
                        "Name: " + name + "\n" +
                        "Student ID: " + id + "\n" +
                        "Admission Number: " + student.getAdmissionNumber() + "\n" +
                        "HS Percentage: " + String.format("%.2f", hsPercentage) + "\n" +
                        "College: " + college.getCollegeName() + "\n" +
                        "Branch: " + branch.getBranchName() + "\n" +
                        "Semester: " + semester;

                showResult(successMessage, SUCCESS_COLOR);
                showAdmissionSuccessDialog(name, id, student.getAdmissionNumber(), hsPercentage,
                        college.getCollegeName(), branch.getBranchName(), semester);
                refreshStudentsTable();
                updateBranchInfo();
                updateAdmissionBranchTable();
                handleBranchCapacity();
                clearAdmissionFormFields();
                showAdmissionStep("college", "Step 1: Select College", " ");
                mainTabs.setSelectedIndex(0);
            } else {
                showResult("FAILED: Admission could not be processed.\nReason: Possible duplicate ID or no seats available in selected semester.", ERROR_COLOR);
            }
        } catch (NumberFormatException e) {
            showResult("ERROR: Invalid numeric format in ID, Age, or HS Percentage.", ERROR_COLOR);
        }
    }

    private void showAdmissionSuccessDialog(String name, int id, int admissionNumber, double hsPercentage,
                                            String college, String branch, String semester) {
        JPanel panel = new RoundedPanel(new BorderLayout(15, 15), new Color(235, 255, 244), new Color(0, 150, 80), 18);
        panel.setBorder(new EmptyBorder(24, 30, 24, 30));
        panel.setPreferredSize(new Dimension(480, 320));

        JLabel title = new JLabel("Admission Successful!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(SUCCESS_COLOR);

        JTextArea details = new JTextArea();
        details.setEditable(false);
        details.setOpaque(false);
        details.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        details.setForeground(new Color(30, 90, 55));
        details.setText(
                "Name: " + name + "\n" +
                "Student ID: " + id + "\n" +
                "Admission Number: " + admissionNumber + "\n" +
                "HS Percentage: " + String.format("%.2f", hsPercentage) + "\n" +
                "College: " + college + "\n" +
                "Branch: " + branch + "\n" +
                "Semester: " + semester
        );

        panel.add(title, BorderLayout.NORTH);
        panel.add(details, BorderLayout.CENTER);
        JOptionPane.showMessageDialog(this, panel, "Admission Successful", JOptionPane.PLAIN_MESSAGE);
    }

    private void setupTableAction(JTable table) {
        table.getColumnModel().getColumn(9).setCellRenderer(new TableButtonRenderer());
        table.getColumnModel().getColumn(9).setCellEditor(new TableButtonEditor(new JCheckBox()));
    }

    // --- Inner classes for Table Button ---

    class TableButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public TableButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setForeground(Color.WHITE);
            setBackground(ERROR_COLOR);
            setBorderPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Delete" : value.toString());
            return this;
        }
    }

    class TableButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;

        public TableButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setForeground(Color.WHITE);
            button.setBackground(ERROR_COLOR);
            button.setBorderPainted(false);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = table.convertRowIndexToModel(row);
            label = (value == null) ? "Delete" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int studentId = (int) studentsTableModel.getValueAt(currentRow, 0);
                String studentName = (String) studentsTableModel.getValueAt(currentRow, 1);
                
                int confirm = JOptionPane.showConfirmDialog(button, 
                    "Are you sure you want to cancel admission for " + studentName + " (ID: " + studentId + ")?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (admissionService.cancelAdmission(studentId, colleges)) {
                        SwingUtilities.invokeLater(() -> {
                            refreshStudentsTable();
                            handleBranchCapacity();
                            updateBranchInfo();
                        });
                    } else {
                        JOptionPane.showMessageDialog(button, "Failed to delete student record.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    class RoundedPanel extends JPanel {
        private final Color fillColor;
        private final Color borderColor;
        private final int radius;

        RoundedPanel(LayoutManager layout, Color fillColor, Color borderColor, int radius) {
            super(layout);
            this.fillColor = fillColor;
            this.borderColor = borderColor;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fillColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void handleViewStudent() {
        String idText = searchIdField.getText().trim();
        if (idText.isEmpty()) return;
        
        try {
            int id = Integer.parseInt(idText);
            Student s = admissionService.getStudentById(id);
            if (s != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("STUDENT PROFILE\n");
                sb.append("===============\n\n");
                sb.append(String.format("%-18s: %d\n", "Student ID", s.getStudentId()));
                sb.append(String.format("%-18s: %s\n", "Admission No", s.getAdmissionNumber()));
                sb.append(String.format("%-18s: %s\n", "Name", s.getName()));
                sb.append(String.format("%-18s: %d\n", "Age", s.getAge()));
                sb.append(String.format("%-18s: %s\n", "Gender", s.getGender()));
                sb.append(String.format("%-18s: %s\n", "HS Percentage",
                        s.getHsPercentage() > 0 ? String.format("%.2f", s.getHsPercentage()) : "Not recorded"));
                sb.append("\nACADEMIC DETAILS\n");
                sb.append("----------------\n");
                sb.append(String.format("%-18s: %s\n", "College", s.getCollegeName()));
                sb.append(String.format("%-18s: %s\n", "Branch", s.getBranchName()));
                sb.append(String.format("%-18s: %s\n", "Semester", s.getSemester()));
                studentDetailsArea.setText(sb.toString());
                studentDetailsArea.setForeground(TEXT_COLOR);
            } else {
                studentDetailsArea.setText("No student found with ID: " + id);
                studentDetailsArea.setForeground(ERROR_COLOR);
            }
        } catch (NumberFormatException e) {
            studentDetailsArea.setText("Invalid ID format. Please enter a number.");
            studentDetailsArea.setForeground(ERROR_COLOR);
        }
    }

    private void handleBranchCapacity() {
        if (capacityTableModel == null) return;
        capacityTableModel.setRowCount(0);
        int idx = capacityCollegeComboBox.getSelectedIndex();
        if (idx < 0) return;
        
        College college = colleges.get(idx);
        int totalFreeS1 = 0, totalCapS1 = 0;
        int totalFreeS3 = 0, totalCapS3 = 0;

        for (Branch b : college.getBranches()) {
            Semester s1 = b.getSemesterByNumber(1);
            Semester s3 = b.getSemesterByNumber(3);
            
            capacityTableModel.addRow(new Object[]{
                b.getBranchName(),
                s1.getTotalCapacity(), s1.getCurrentStudentCount(), s1.getRemainingSeats(),
                s3.getTotalCapacity(), s3.getCurrentStudentCount(), s3.getRemainingSeats()
            });
            
            totalCapS1 += s1.getTotalCapacity();
            totalFreeS1 += s1.getRemainingSeats();
            totalCapS3 += s3.getTotalCapacity();
            totalFreeS3 += s3.getRemainingSeats();
        }
        
        capacitySummaryLabel.setText(String.format("Total Free - 1st SEM: %d/%d | 3rd SEM: %d/%d", 
                totalFreeS1, totalCapS1, totalFreeS3, totalCapS3));
    }

    private void refreshStudentsTable() {
        if (studentsTableModel == null) return;
        studentsTableModel.setRowCount(0);
        List<Student> all = admissionService.getAllStudents();
        for (Student s : all) {
            studentsTableModel.addRow(new Object[]{
                s.getStudentId(), s.getName(), s.getAge(), s.getGender(),
                s.getHsPercentage() > 0 ? String.format("%.2f", s.getHsPercentage()) : "-",
                s.getCollegeName(), s.getBranchName(), s.getSemester(), s.getAdmissionNumber(),
                "Delete"
            });
        }
        studentCountLabel.setText("Total Students: " + all.size());
    }

    private void filterStudentsTable() {
        String text = studentsFilterField.getText().trim();
        if (text.isEmpty()) {
            studentSorter.setRowFilter(null);
        } else {
            studentSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
        }
        studentCountLabel.setText("Showing: " + studentsTable.getRowCount() + " / Total: " + admissionService.getTotalStudents());
    }

    private void updateBranchCombo() {
        branchComboBox.removeAllItems();
        int idx = collegeComboBox.getSelectedIndex();
        if (idx >= 0) {
            colleges.get(idx).getBranches().forEach(b -> branchComboBox.addItem(b.getBranchName()));
        }
        updateBranchInfo();
    }

    private void updateAdmissionBranchTable() {
        if (admissionBranchTableModel == null || collegeComboBox == null) return;
        admissionBranchTableModel.setRowCount(0);
        int collegeIndex = collegeComboBox.getSelectedIndex();
        if (collegeIndex < 0) return;

        String semesterText = (String) semesterComboBox.getSelectedItem();
        int semesterNumber = semesterText != null && semesterText.contains("1") ? 1 : 3;
        admissionBranchTableModel.setColumnIdentifiers(new String[]{
                "Branch", semesterText + " Free Seats", semesterText + " Total Capacity"
        });
        if (admissionBranchTable != null) {
            styleTable(admissionBranchTable);
        }

        for (Branch branch : colleges.get(collegeIndex).getBranches()) {
            Semester semester = branch.getSemesterByNumber(semesterNumber);
            admissionBranchTableModel.addRow(new Object[]{
                    branch.getBranchName(),
                    semester.getRemainingSeats(),
                    semester.getTotalCapacity()
            });
        }
    }

    private void updateBranchInfo() {
        int cIdx = collegeComboBox.getSelectedIndex();
        int bIdx = branchComboBox.getSelectedIndex();
        if (branchInfoLabel == null || semesterComboBox == null || cIdx < 0 || bIdx < 0) return;

        Branch b = colleges.get(cIdx).getBranches().get(bIdx);
        String semStr = (String) semesterComboBox.getSelectedItem();
        int semNum = semStr.contains("1") ? 1 : 3;
        
        Semester s = b.getSemesterByNumber(semNum);
        if (s != null) {
            branchInfoLabel.setText(String.format("Available: %d out of %d seats", s.getRemainingSeats(), s.getTotalCapacity()));
            branchInfoLabel.setForeground(s.getRemainingSeats() > 0 ? SUCCESS_COLOR : ERROR_COLOR);
        }
    }

    private String getSelectedCollegeName() {
        Object selected = collegeComboBox.getSelectedItem();
        return selected == null ? "" : selected.toString();
    }

    private String getSelectedBranchName() {
        Object selected = branchComboBox.getSelectedItem();
        return selected == null ? "" : selected.toString();
    }

    private void showResult(String msg, Color color) {
        admissionResultArea.setText(msg);
        admissionResultArea.setForeground(color);
        if (admissionResultScrollPane != null) {
            admissionResultScrollPane.setVisible(true);
            admissionResultScrollPane.getParent().revalidate();
        }
    }

    private void clearAdmissionForm() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderComboBox.setSelectedIndex(0);
        hsPercentageField.setText("");
        admissionResultArea.setText("");
        updateBranchInfo();
    }

    private void clearAdmissionFormFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderComboBox.setSelectedIndex(0);
        hsPercentageField.setText("");
    }

    private Optional<String> validateInputs() {
        String idText = idField.getText().trim();
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String hsText = hsPercentageField.getText().trim();
        
        if (idText.isEmpty()) return Optional.of("Student ID is required.");
        if (name.isEmpty()) return Optional.of("Student Name is required.");
        if (ageText.isEmpty()) return Optional.of("Age is required.");
        if (genderComboBox.getSelectedIndex() == 0) return Optional.of("Please select a gender.");
        if (hsText.isEmpty()) return Optional.of("HS Percentage is required.");
        
        try {
            int id = Integer.parseInt(idText);
            if (id <= 0) return Optional.of("Student ID must be a positive number.");
            if (admissionService.getStudentById(id) != null) return Optional.of("Student ID already exists.");
        } catch (NumberFormatException e) {
            return Optional.of("Student ID must be a valid number.");
        }
        
        try {
            int age = Integer.parseInt(ageText);
            if (age < 15 || age > 45) return Optional.of("Age must be between 15 and 45.");
        } catch (NumberFormatException e) {
            return Optional.of("Age must be a valid number.");
        }

        try {
            double hsPercentage = Double.parseDouble(hsText);
            if (hsPercentage < 0 || hsPercentage > 100) return Optional.of("HS Percentage must be between 0 and 100.");
            if (hsPercentage < 45) return Optional.of("Admission not allowed. HS Percentage must be at least 45.");
        } catch (NumberFormatException e) {
            return Optional.of("HS Percentage must be a valid number.");
        }
        
        return Optional.empty();
    }

    private String[] getCollegeNames() {
        return colleges.stream().map(College::getCollegeName).toArray(String[]::new);
    }

    private String[] getSemesterNames() {
        return new String[]{"1st SEM", "3rd SEM (Lateral Entry)"};
    }
}
