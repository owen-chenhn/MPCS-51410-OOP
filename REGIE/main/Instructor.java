package REGIE.main;

import java.util.*;

interface InstructorInterface {
    /* Interface of the responsibility of Instructor class. */
    public boolean assignGrade(String courseId, String studentId, String grade);
    public boolean editCourseDescription(String course, String des);
    public String reportGrades(String courseId);
    public String viewRequests();
    public boolean replyRequest(String courseId, String studentId, boolean consent);
}

public class Instructor extends User implements InstructorInterface {
    /* Class that represents an instructor account. */
    protected Instructor(String id, String name, String password, String department) {
        super(id, name, password, department, "instructor");
    }

    /* Assign or modify grade for a student. */
    public boolean assignGrade(String courseId, String studentId, String grade) {
        if (db.addGrade(courseId, studentId, grade)) {
            System.out.println("Assign/Modify grade succeeded.");
            return true;
        }
        else {
            System.out.println("Error: database operation failed.");
            return false;
        }
    }

    /* Edit course description. */
    public boolean editCourseDescription(String course, String des) {
        if (db.editDescription(course, des)) {
            System.out.println("Editing course description succeeded.");
            return true;
        }
        else {
            System.out.println("Error: database operation failed.");
            return false;
        }
    }
    
    /* Report grades of all the students of a course to university. */
    public String reportGrades(String courseId) {
        Map<String, String> grades = db.getGrades(null, courseId);
        String report = String.format("\n==============Grades Report==============\nCourse: %s    Instructor: %s\n\n", courseId, name);
        for (Map.Entry<String, String> entry: grades.entrySet()) {
            report += "Student: " + entry.getKey() + "    " + entry.getValue() + "\n";
        }
        return report;
    }

    /* View student requests of all courses that the instructor teaches. */
    public String viewRequests() {
        String request = "\n==============Student Requests==============\nstudentId\tname\tcourseName\n";
        for (String s: db.getRequests(id)) {
            request += s;
        }
        return request;
    }

    /* Reply a request of a student taking a course. */
    public boolean replyRequest(String studentId, String courseId, boolean consent) {
        if (db.processRequest(studentId, courseId, consent)) {
            System.out.println("Replying student request succeeded.");
            return true;
        }
        else {
            System.out.println("Error: database operation failed.");
            return false;
        }
    }
}