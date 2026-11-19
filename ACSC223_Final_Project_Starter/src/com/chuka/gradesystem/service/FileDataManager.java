package com.chuka.gradesystem.service;

import com.chuka.gradesystem.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FileDataManager - File-based Implementation of DataManager Interface
 * 
 * This class demonstrates INTERFACE IMPLEMENTATION and FILE I/O.
 * 
 * Key OOP Concepts:
 * 1. INTERFACE IMPLEMENTATION - Implements all DataManager methods
 * 2. POLYMORPHISM - Can be used wherever DataManager is expected
 * 3. ENCAPSULATION - File operations encapsulated in private methods
 * 4. SERIALIZATION - Objects are saved to files using Java serialization
 * 
 * @author Charles David
 * @course ACSC 223 - Object Oriented Programming (Java 1)
 * @institution Chuka University
 */
public class FileDataManager implements DataManager {
    
    // File paths for data storage
    private static final String DATA_DIR = "data/";
    private static final String STUDENTS_FILE = DATA_DIR + "students.dat";
    private static final String COURSES_FILE = DATA_DIR + "courses.dat";
    private static final String GRADES_FILE = DATA_DIR + "grades.dat";
    
    // In-memory storage (loaded from files)
    private List<Student> students;
    private List<Course> courses;
    private List<Grade> grades;
    
    /**
     * Constructor - Initialize data structures and load data
     */
    public FileDataManager() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        grades = new ArrayList<>();
        
        // Create data directory if it doesn't exist
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Load existing data
        loadAllData();
    }
    
    // ========== STUDENT OPERATIONS ==========
    
    @Override
    public boolean saveStudent(Student student) {
        if (student == null) {
            return false;
        }
        
        // Check if student already exists
        Student existing = findStudentById(student.getStudentId());
        if (existing != null) {
            // Update existing student
            return updateStudent(student);
        }
        
        students.add(student);
        return saveStudentsToFile();
    }
    
    @Override
    public List<Student> loadAllStudents() {
        return new ArrayList<>(students);
    }
    
    @Override
    public Student findStudentById(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }
    
    @Override
    public boolean updateStudent(Student student) {
        if (student == null) {
            return false;
        }
        
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(student.getStudentId())) {
                students.set(i, student);
                return saveStudentsToFile();
            }
        }
        
        return false;
    }
    
    @Override
    public boolean deleteStudent(String studentId) {
        boolean removed = students.removeIf(s -> s.getStudentId().equals(studentId));
        if (removed) {
            // Also remove all grades for this student
            grades.removeIf(g -> g.getStudent().getStudentId().equals(studentId));
            saveGradesToFile();
            return saveStudentsToFile();
        }
        return false;
    }
    
    // ========== COURSE OPERATIONS ==========
    
    @Override
    public boolean saveCourse(Course course) {
        if (course == null) {
            return false;
        }
        
        Course existing = findCourseByCode(course.getCourseCode());
        if (existing != null) {
            return updateCourse(course);
        }
        
        courses.add(course);
        return saveCoursesToFile();
    }
    
    @Override
    public List<Course> loadAllCourses() {
        return new ArrayList<>(courses);
    }
    
    @Override
    public Course findCourseByCode(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equals(courseCode)) {
                return course;
            }
        }
        return null;
    }
    
    @Override
    public boolean updateCourse(Course course) {
        if (course == null) {
            return false;
        }
        
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseCode().equals(course.getCourseCode())) {
                courses.set(i, course);
                return saveCoursesToFile();
            }
        }
        
        return false;
    }
    
    @Override
    public boolean deleteCourse(String courseCode) {
        boolean removed = courses.removeIf(c -> c.getCourseCode().equals(courseCode));
        if (removed) {
            // Also remove all grades for this course
            grades.removeIf(g -> g.getCourse().getCourseCode().equals(courseCode));
            saveGradesToFile();
            return saveCoursesToFile();
        }
        return false;
    }
    
    // ========== GRADE OPERATIONS ==========
    
    @Override
    public boolean saveGrade(Grade grade) {
        if (grade == null || grade.getStudent() == null || grade.getCourse() == null) {
            return false;
        }
        
        // Remove existing grade for same student and course
        grades.removeIf(g -> 
            g.getStudent().getStudentId().equals(grade.getStudent().getStudentId()) &&
            g.getCourse().getCourseCode().equals(grade.getCourse().getCourseCode())
        );
        
        grades.add(grade);
        
        // Also update the student's grade list
        Student student = findStudentById(grade.getStudent().getStudentId());
        if (student != null) {
            student.addGrade(grade);
            updateStudent(student);
        }
        
        return saveGradesToFile();
    }
    
    @Override
    public List<Grade> loadAllGrades() {
        return new ArrayList<>(grades);
    }
    
    @Override
    public List<Grade> getGradesByStudent(String studentId) {
        return grades.stream()
                .filter(g -> g.getStudent().getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Grade> getGradesByCourse(String courseCode) {
        return grades.stream()
                .filter(g -> g.getCourse().getCourseCode().equals(courseCode))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean deleteGrade(Student student, Course course) {
        boolean removed = grades.removeIf(g ->
            g.getStudent().getStudentId().equals(student.getStudentId()) &&
            g.getCourse().getCourseCode().equals(course.getCourseCode())
        );
        
        if (removed) {
            return saveGradesToFile();
        }
        return false;
    }
    
    // ========== UTILITY OPERATIONS ==========
    
    @Override
    public boolean saveAllData() {
        boolean success = true;
        success &= saveStudentsToFile();
        success &= saveCoursesToFile();
        success &= saveGradesToFile();
        return success;
    }
    
    @Override
    public boolean loadAllData() {
        boolean success = true;
        success &= loadStudentsFromFile();
        success &= loadCoursesFromFile();
        success &= loadGradesFromFile();
        return success;
    }
    
    @Override
    public boolean clearAllData() {
        students.clear();
        courses.clear();
        grades.clear();
        return saveAllData();
    }
    
    // ========== PRIVATE FILE I/O METHODS ==========
    
    /**
     * Save students to file using serialization
     * @return true if successful
     */
    private boolean saveStudentsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(STUDENTS_FILE))) {
            oos.writeObject(students);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load students from file
     * @return true if successful
     */
    @SuppressWarnings("unchecked")
    private boolean loadStudentsFromFile() {
        File file = new File(STUDENTS_FILE);
        if (!file.exists()) {
            return true; // No file yet, not an error
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(STUDENTS_FILE))) {
            students = (List<Student>) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading students: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Save courses to file
     * @return true if successful
     */
    private boolean saveCoursesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(COURSES_FILE))) {
            oos.writeObject(courses);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load courses from file
     * @return true if successful
     */
    @SuppressWarnings("unchecked")
    private boolean loadCoursesFromFile() {
        File file = new File(COURSES_FILE);
        if (!file.exists()) {
            return true;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(COURSES_FILE))) {
            courses = (List<Course>) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading courses: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Save grades to file
     * @return true if successful
     */
    private boolean saveGradesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(GRADES_FILE))) {
            oos.writeObject(grades);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving grades: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load grades from file
     * @return true if successful
     */
    @SuppressWarnings("unchecked")
    private boolean loadGradesFromFile() {
        File file = new File(GRADES_FILE);
        if (!file.exists()) {
            return true;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(GRADES_FILE))) {
            grades = (List<Grade>) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading grades: " + e.getMessage());
            return false;
        }
    }
}
