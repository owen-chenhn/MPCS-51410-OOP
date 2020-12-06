package REGIE.main;

import java.util.*;


interface CommonUserInterface {
    /*  Interface of the responsibility shared by both Student class and Instructor class. 
        It also follows the Interface Segregation Principle. 
    */
    public List<Course> browseCourses(String courseName, String department, String time, String room, String instrName);
    public List<Course> involvedCourses();
    public boolean modifyAccount(String newName, String newPsd, String newDepmt);
    public Course getCourseInfo(String courseId);
}

abstract public class User implements CommonUserInterface {
    /* Base class of user object. This class should be extended by Student class and Instructor class. */
    public String name;
    public String id;
    protected String password;
    public String department;
    public String role;
    protected DatabaseProxy db;

    protected User(String id, String name, String password, String department, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.department = department;
        this.role = role;
        this.db = DatabaseProxy.getInstance();
    }
 
    public List<Course> browseCourses(String courseName, String department, String time, String room, String instrName) {
        /*  browse available courses according to course name, department, time, location and instructor name.  
            Set to null if this search criteria is not provided. 
        */ 
        Map<String, String> filter = new HashMap<>();
        if (courseName != null) 
            filter.put("course_name", courseName);
        if (department != null) 
            filter.put("course.department", department);
        if (time != null) 
            filter.put("course_time", time);
        if (room != null) 
            filter.put("room", room);
        if (instrName != null) 
            filter.put("name", instrName);
        
        return db.getAllCourses(filter);
    }

    public List<Course> involvedCourses() {
        /* Get the involved courses of the user. This depends on the role (students or instructors) of the user.  */
        return db.getInvolvedCourses(id, role);
    }
    
    public boolean modifyAccount(String newName, String newPsd, String newDepmt) {
        /* Method to modify account information. The field that does not need to get midified should set to null. */ 
        Map<String, String> newData = new HashMap<>();
        if (newName != null) { 
            newData.put("name", newName);
            name = newName;
        }
        if (newPsd != null) { 
            newData.put("password", newPsd); 
            password = newPsd;
        }
        if (newDepmt != null) { 
            newData.put("department", newDepmt); 
            department = newDepmt;
        }
        
        if (newData.size() > 0) {
            if (db.updateAccount(newData, id)) {
                if (newName != null) { System.out.println("User name is changed to: " + newName); }
                if (newPsd != null) { System.out.println("Password is changed."); }
                if (newDepmt != null) { System.out.println("Department is changed to: " + newDepmt); }
                return true;
            }
            else {
                System.out.println("Account modification failed.");
                return false;
            }
        }
        else { 
            System.out.println("Nothing modified.");
            return true; 
        }
    }

    public Course getCourseInfo(String courseId) {
        /* Get information about a single course. */
        return db.getSingleCourse(courseId);
    }
}