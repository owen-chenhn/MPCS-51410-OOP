package REGIE.main;

import java.util.*;

interface StudentInterface {
    /* Interface of the responsibility of Student class. */
    public boolean registerCourse(String courseId);
    public boolean dropCourse(String courseId);
    public String viewGrade(String courseId);
    public String viewTranscript();
    public boolean askConsent(String courseId);
}

public class Student extends User implements StudentInterface {
    /* Class that represents a student account. */
    List<Integer> restrictions;     // Restriction is represented with integer. A map from restricId to restriction detail is saved in database. 

    protected Student(String id, String name, String password, String department) {
        super(id, name, password, department, "student");
        restrictions = null;
    }
    
    /* Register a course. */
    public boolean registerCourse(String courseId) {
        // Check the number of courses the student has registered. 
        List<Course> courses = involvedCourses();
        if (courses.size() == 3) {
            System.out.println("Registration failed: The number of courses you registered (3) reaches limit.");
            return false;
        }
        // Check whether the student has registered this course.
        for (Course c: courses) {
            if (courseId.equals(c.courseId)) {
                System.out.println("Registration failed: you have already registered this course.");
                return false;
            }
        }
        // Check whether the student fulfills the prerequisite requirements.
        Map<String, String> grades = db.getGrades(id, null);
        List<String> preqs = db.getPrerequisites(courseId);
        List<String> reqired_preqs = new LinkedList<>();
        for (String preq: preqs) {
            if (!grades.containsKey(preq)) { reqired_preqs.add(preq); }
        }
        if (reqired_preqs.size() > 0) {
            System.out.println("Registration failed: the following prerequisites are required:");
            for (String required: reqired_preqs) { System.out.printf("%s ", required); }
            System.out.println();
            return false;
        }
        // Check whether the course is filled. 
        Course c = db.getSingleCourse(courseId);
        if (c.regNum == c.maxNum) {
            System.out.println("Registration failed: the course is filled.");
            return false;
        }

        // All checks are passed. Register the course. 
        if (db.register(id, courseId, c.regNum+1)) {
            System.out.printf("Registering course %s succeeded.\n", courseId);
            return true;
        }
        else {
            System.out.println("Database operation failed.");
            return false;
        }
    }

    /* Drop a course. */
    public boolean dropCourse(String courseId) {
        // Check whether the student has registered this course.
        List<Course> courses = db.getInvolvedCourses(id, role);
        boolean flag = false;
        int num = -1;            // store the number of student registering the course. 
        for (Course c: courses) {
            if (courseId.equals(c.courseId)) { 
                flag = true; 
                num = c.regNum;
                break; 
            }
        }
        if (!flag) {
            System.out.println("Drop failed: the course is not registered.");
            return false;
        }

        if (db.drop(id, courseId, num-1)) {
            System.out.printf("Dropping course %s succeeded.\n", courseId);
            return true;
        }
        else {
            System.out.println("Database operation failed.");
            return false;
        }
    }

    /* View grade of a course that the student took. */ 
    public String viewGrade(String courseId) {
        String grade = db.getGrades(id, courseId).get("grade");
        //Course c = db.getSingleCourse(courseId);
        String result;
        if (grade != null) { result = "Course Id: " + courseId + ", Grade: " + grade; }
        else { result = "Error: The student " + name + " didn't take course " + courseId; }
        
        return result;
    }

    public String viewTranscript() {
        String result = String.format("\n==============Student Transcript==============\nName: %s\nId: %s\n", name, id);
        Map<String, String> grades = db.getGrades(id, null);
        for (Map.Entry<String, String> entry: grades.entrySet()) {
            result += "Course: " + entry.getKey() + "    " + entry.getValue() + "\n";
        }
        return result;
    }

    /* Ask consent to register for a course. The consent request will automatically be sent to the instructor of the course. */
    public boolean askConsent(String courseId) {
        if (db.addRequest(id, courseId)) {
            System.out.printf("Request for consent is sent.\n");
            return true;
        }
        else {
            System.out.println("Database operation failed.");
            return false;
        }
    }
}