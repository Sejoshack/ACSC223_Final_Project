package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.model.Course;
import com.chuka.gradesystem.service.DataManager;
import com.chuka.gradesystem.util.Constants;
import com.chuka.gradesystem.util.ValidationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;


/**
 * CoursePanel - Course Management Interface
 * 
 * TODO: Students should implement this panel following the StudentPanel example
 * 
 * Required Features:
 * - JTable to display courses
 * - Form to add/edit courses (Course Code, Name, Credits, Instructor)
 * - Buttons: Add, Update, Delete, Clear
 * - Search functionality
 * - Input validation using ValidationUtil
 * 
 * @author Charles David
 * @course ACSC 223 - Object Oriented Programming (Java 1)
 */
public class CoursePanel extends JPanel implements ActionListener, KeyListener {
    
    private DataManager dataManager;

    private JTable coursTable;
    private DefaultTableModel tableModel;
    private JScrollPane tabScroll;

    private JTextField courseCodeField;
    private JTextField courseNameField;
    private JSpinner creditsSpinner;
    private JTextField instructorField;
    private JTextField searchField;

    private JButton addButton, updateButton, deleteButton, clearButton;
    
    public CoursePanel(DataManager dataManager) {
        this.dataManager = dataManager;
        
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        // TODO: Initialize components similar to StudentPanel
        
        // Placeholder message
        JLabel label = new JLabel("Course Management Panel - To Be Implemented", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
        
        JTextArea instructions = new JTextArea(
            "INSTRUCTIONS FOR STUDENTS:\n\n" +
            "1. Create a JTable to display courses\n" +
            "2. Create a form with fields: Course Code, Course Name, Credits, Instructor\n" +
            "3. Add buttons: Add Course, Update, Delete, Clear\n" +
            "4. Implement ActionListener for button clicks\n" +
            "5. Add search functionality with KeyListener\n" +
            "6. Use ValidationUtil to validate course code and credits\n" +
            "7. Use DataManager to save/load/update/delete courses\n\n" +
            "Refer to StudentPanel.java as a complete example!"
        );
        instructions.setEditable(false);
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 12));
        instructions.setMargin(new Insets(20, 20, 20, 20));
        add(new JScrollPane(instructions), BorderLayout.SOUTH);
    }
    
    public void refreshData() {
        // TODO: Refresh course table
    }
}
