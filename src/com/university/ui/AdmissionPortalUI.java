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

    // Form fields
    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> collegeComboBox;
    private JComboBox<String> branchComboBox;
    private JComboBox<String> semesterComboBox;
    private JTextArea admissionResultArea;
    private JLabel branchInfoLabel;

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
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        styleTabbedPane(tabs);

        tabs.addTab("New Admission", createAdmissionPanel());
        tabs.addTab("All Students", createAllStudentsPanel());
        tabs.addTab("View Student", createViewStudentPanel());
        tabs.addTab("Branch Capacity", createBranchCapacityPanel());

        add(tabs);
        
        // Initial data load
        refreshStudentsTable();
        handleBranchCapacity();
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

        // Form Container to center the form
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(550, 500));
        formPanel.setBackground(Color.WHITE);
        TitledBorder border = BorderFactory.createTitledBorder("Admission Registration Form");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        border.setTitleColor(PRIMARY_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 230, 240), 2, true),
            new EmptyBorder(25, 35, 25, 35)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(15);
        nameField = new JTextField(20);
        ageField = new JTextField(15);
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        collegeComboBox = new JComboBox<>(getCollegeNames());
        branchComboBox = new JComboBox<>();
        semesterComboBox = new JComboBox<>(getSemesterNames());
        branchInfoLabel = new JLabel(" ");
        branchInfoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        collegeComboBox.addActionListener(e -> updateBranchCombo());
        branchComboBox.addActionListener(e -> updateBranchInfo());
        semesterComboBox.addActionListener(e -> updateBranchInfo());
        updateBranchCombo();

        int row = 0;
        addFormField(formPanel, "Student ID:", idField, gbc, row++);
        addFormField(formPanel, "Full Name:", nameField, gbc, row++);
        addFormField(formPanel, "Age:", ageField, gbc, row++);
        addFormField(formPanel, "Gender:", genderComboBox, gbc, row++);
        addFormField(formPanel, "College:", collegeComboBox, gbc, row++);
        addFormField(formPanel, "Branch:", branchComboBox, gbc, row++);
        
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(branchInfoLabel, gbc);

        addFormField(formPanel, "Admission Semester:", semesterComboBox, gbc, row++);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        actionPanel.setOpaque(false);
        JButton admitButton = new JButton("Admit Student");
        JButton clearButton = new JButton("Clear Form");
        styleButton(admitButton);
        styleButton(clearButton);
        admitButton.addActionListener(this::handleAdmission);
        clearButton.addActionListener(e -> clearAdmissionForm());
        actionPanel.add(admitButton);
        actionPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(actionPanel, gbc);

        centerWrapper.add(formPanel);

        admissionResultArea = new JTextArea(6, 0);
        admissionResultArea.setEditable(false);
        admissionResultArea.setBackground(new Color(252, 252, 252));
        JScrollPane resultScroll = new JScrollPane(admissionResultArea);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Status & Log"));

        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        mainPanel.add(resultScroll, BorderLayout.SOUTH);

        return mainPanel;
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

        String[] columns = {"ID", "Name", "Age", "Gender", "College", "Branch", "Semester", "Admn No.", "Action"};
        studentsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return column == 8; // Only Action column is editable for the button
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
            String gender = (String) genderComboBox.getSelectedItem();
            
            int collegeIdx = collegeComboBox.getSelectedIndex();
            College college = colleges.get(collegeIdx);
            
            int branchIdx = branchComboBox.getSelectedIndex();
            Branch branch = college.getBranches().get(branchIdx);
            
            String semester = (String) semesterComboBox.getSelectedItem();

            Student student = new Student(id, name, age, gender, college.getCollegeName(), branch.getBranchName(), semester);
            
            boolean success = admissionService.processAdmission(student, branch);
            if (success) {
                showResult("SUCCESS: Admission processed for " + name + " (ID: " + id + ")\n" +
                          "Admission Number: " + student.getAdmissionNumber() + "\n" +
                          "College: " + college.getCollegeName() + "\n" +
                          "Branch: " + branch.getBranchName() + "\n" +
                          "Semester: " + semester, SUCCESS_COLOR);
                refreshStudentsTable();
                updateBranchInfo();
                handleBranchCapacity();
                clearAdmissionFormFields();
            } else {
                showResult("FAILED: Admission could not be processed.\nReason: Possible duplicate ID or no seats available in selected semester.", ERROR_COLOR);
            }
        } catch (NumberFormatException e) {
            showResult("ERROR: Invalid numeric format in ID or Age.", ERROR_COLOR);
        }
    }

    private void setupTableAction(JTable table) {
        table.getColumnModel().getColumn(8).setCellRenderer(new TableButtonRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new TableButtonEditor(new JCheckBox()));
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

    private void updateBranchInfo() {
        int cIdx = collegeComboBox.getSelectedIndex();
        int bIdx = branchComboBox.getSelectedIndex();
        if (cIdx < 0 || bIdx < 0) return;

        Branch b = colleges.get(cIdx).getBranches().get(bIdx);
        String semStr = (String) semesterComboBox.getSelectedItem();
        int semNum = semStr.contains("1") ? 1 : 3;
        
        Semester s = b.getSemesterByNumber(semNum);
        if (s != null) {
            branchInfoLabel.setText(String.format("Available: %d out of %d seats", s.getRemainingSeats(), s.getTotalCapacity()));
            branchInfoLabel.setForeground(s.getRemainingSeats() > 0 ? SUCCESS_COLOR : ERROR_COLOR);
        }
    }

    private void showResult(String msg, Color color) {
        admissionResultArea.setText(msg);
        admissionResultArea.setForeground(color);
    }

    private void clearAdmissionForm() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        admissionResultArea.setText("");
        updateBranchInfo();
    }

    private void clearAdmissionFormFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
    }

    private Optional<String> validateInputs() {
        String idText = idField.getText().trim();
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        
        if (idText.isEmpty()) return Optional.of("Student ID is required.");
        if (name.isEmpty()) return Optional.of("Student Name is required.");
        if (ageText.isEmpty()) return Optional.of("Age is required.");
        
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
        
        return Optional.empty();
    }

    private String[] getCollegeNames() {
        return colleges.stream().map(College::getCollegeName).toArray(String[]::new);
    }

    private String[] getSemesterNames() {
        return new String[]{"1st SEM", "3rd SEM (Lateral Entry)"};
    }
}
