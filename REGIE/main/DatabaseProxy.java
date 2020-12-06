package REGIE.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;


public class DatabaseProxy {
    /*  Class that handles all the database operations. 
        The class itself expresses the usage of pattern singleton and proxy.
        Some methods of the class also apply pattern factory (method). 
    */
    static DatabaseProxy _db = null;

    // database configuration
    private String host = "localhost";
    private String port = "3306";
    private String database = "regie";		// Database to use
    private String connectionUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";	// "jdbc:mysql://localhost:3306/regie?useSSL=false"
    private String connectionUser = "root";
    private String connectionPassword = "OHchnjyshpc123";

    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    
    /* Initialize configuration of JDBC driver */
    private DatabaseProxy() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        conn = null;
        stmt = null;
        rs = null;
    }

    /* Implementation of design pattern Singleton. */
    static public DatabaseProxy getInstance() {     
        if (_db == null) {
            _db = new DatabaseProxy();
        }
        return _db;
    }

    /* Validate userId and password. It is also a factory method for instantiating a User object. This method is used in LoginEngine and is tested at there. */
    User validateUser(String userId, String password) {
        User user = null;
        // Construct query:
        String query = String.format("SELECT * FROM user WHERE id = '%s' AND password = '%s';", userId, password);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            // Query result processing:
            if (rs.next()) {
                if (rs.getString("role").equals("student")) {
                    user = new Student(rs.getString("id"), rs.getString("name"), rs.getString("password"), rs.getString("department"));
                }
                else {
                    user = new Instructor(rs.getString("id"), rs.getString("name"), rs.getString("password"), rs.getString("department"));
                }
            }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return user;
    }

    /* Query and return a single course. It is also a factory method to instantiate a Course object. */
    Course getSingleCourse(String courseId) {
        Course c = null;
        String query = String.format("SELECT * FROM course JOIN user ON course.instructorId = user.id WHERE courseId = '%s';", courseId);

        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            // Query result processing:
            if (rs.next()) {
                c = new Course(rs.getString("course_name"), rs.getString("courseId"), rs.getString("name"), rs.getString("course.department"), rs.getString("course_time"), 
                                        rs.getString("room"), rs.getInt("maxNum"), rs.getInt("regNum"), rs.getString("course_description"));
            }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return c;
    }

    /* Search available courses according to criteria in filter. Tested at class Student or Instructor. */
    List<Course> getAllCourses(Map<String, String> filter) {
        List<Course> courses = new LinkedList<>();

        String query = "SELECT * FROM course JOIN user ON course.instructorId = user.id";
        if (filter.size() > 0) {
            query += " WHERE ";
            int i = filter.size();
            for (Map.Entry<String, String> entry: filter.entrySet()) {
                query += entry.getKey() + "= '" + entry.getValue() + "'";
                i--;
                if (i > 0) { query += " AND "; }
            }
        }
        query += ";";
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            // Query result processing:
            while (rs.next()) {
                courses.add(new Course(rs.getString("course_name"), rs.getString("courseId"), rs.getString("name"), rs.getString("course.department"), rs.getString("course_time"), 
                                        rs.getString("room"), rs.getInt("maxNum"), rs.getInt("regNum"), rs.getString("course_description")));
            }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return courses;
    }

    /* Query all the courses that a user involved. As for students, they are the courses that are taken; as for instructors, they are the courses that are taught. */
    List<Course> getInvolvedCourses(String id, String role) {
        List<Course> courses = new LinkedList<>();
        String query;
        if (role.equals("student")) {
            query = String.format("(SELECT * FROM registrationList NATURAL JOIN course WHERE studentId = '%s') c", id);
        }
        else {
            query = String.format("(SELECT * FROM course WHERE instructorId = '%s') c", id);
        }
        query = "SELECT * FROM " + query + " JOIN user on c.instructorId = user.id;";
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            // Query result processing:
            while (rs.next()) {
                courses.add(new Course(rs.getString("course_name"), rs.getString("courseId"), rs.getString("name"), rs.getString("c.department"), rs.getString("course_time"), 
                                        rs.getString("room"), rs.getInt("maxNum"), rs.getInt("regNum"), rs.getString("course_description")));
            }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return courses;
    }

    /* Update new user account data to database. */
    boolean updateAccount(Map<String, String> newData, String id) {
        boolean result = false;
        String query = "UPDATE user SET ";
        int i = newData.size();
        for (Map.Entry<String, String> entry: newData.entrySet()) {
            query += String.format("%s='%s'", entry.getKey(), entry.getValue());
            i--;
            if (i > 0) { query += ", "; }
        }
        query += " WHERE id='" + id + "';";
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            int n = stmt.executeUpdate(query);

            // Query result processing:
            if (n > 0) { result = true; }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    /* Get student grades. */
    Map<String, String> getGrades(String studentId, String courseId) {
        /* This method can be called in three ways: 
            1. If both studentId and courseId are not null, the specified student's grade of the course is queried, and the returned 
                map has a single entry: <"grade", grade>.
            2. If studentId is null, all student grades of the specified course is returned in the map, with entry content being <studentId, grade>. 
            3. If courseId is null, the specified student's grade history of all the courses the student has taken is returned in the map, with entry <courseId, grade>. 
        */
        Map<String, String> grades = new HashMap<>();
        String query;
        if (courseId == null) {
            query = String.format("SELECT * FROM gradeList WHERE studentId='%s';", studentId);
        }
        else if (studentId == null) {
            query = String.format("SELECT * FROM gradeList WHERE courseId='%s';", courseId);
        }
        else {
            query = String.format("SELECT * FROM gradeList WHERE studentId='%s' AND courseId='%s';", studentId, courseId);
        }
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            // Query result processing:
            while (rs.next()) {
                if (courseId == null) { grades.put(rs.getString("courseId"), rs.getString("grade")); }
                else if (studentId == null) { grades.put(rs.getString("studentId"), rs.getString("grade")); }
                else { grades.put("grade", rs.getString("grade")); }
            }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return grades;
    }

    /* Get the prerequisits of a course. */
    List<String> getPrerequisites(String courseId) {
        List<String> preqs = new LinkedList<>();
        String query = String.format("SELECT * FROM prerequisiteList WHERE courseId='%s';", courseId);
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            // Query result processing:
            while (rs.next()) {
                preqs.add(rs.getString("prereq_course"));
            }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return preqs;
    }

    /* Register a course for a student, by adding an entry to table registrationList. */
    boolean register(String studentId, String courseId, int num) {
        boolean result = false;
        String query = String.format("INSERT INTO registrationList VALUES ('%s', '%s');", studentId, courseId);
        String query2 = String.format("UPDATE course SET regNum=%s WHERE courseId='%s'", num, courseId);
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            int n = stmt.executeUpdate(query);
            int m = stmt.executeUpdate(query2);

            // Query result processing:
            if (n > 0 && m > 0) { result = true; }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    /* drop a course for a student. */
    boolean drop(String studentId, String courseId, int num) {
        boolean result = false;
        String query = String.format("DELETE FROM registrationList WHERE studentId='%s' AND courseId='%s';", studentId, courseId);
        String query2 = String.format("UPDATE course SET regNum=%s WHERE courseId='%s'", num, courseId);
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            int n = stmt.executeUpdate(query);
            int m = stmt.executeUpdate(query2);

            // Query result processing:
            if (n > 0 && m > 0) { result = true; }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    /* Add a grade record to table gradeList. */
    boolean addGrade(String courseId, String studentId, String grade) {
        boolean result = false;
        String query;
        if (getGrades(studentId, courseId).size() == 0) {
            query = String.format("INSERT INTO gradeList VALUES ('%s', '%s', '%s');", studentId, courseId, grade);
        }
        else {
            query = String.format("UPDATE gradeList SET grade='%s' WHERE studentId='%s' AND courseId='%s';", grade, studentId, courseId);
        }
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            int n = stmt.executeUpdate(query);

            // Query result processing:
            if (n > 0) { result = true; }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    /* Update course description. */
    boolean editDescription(String courseId, String descrip) {
        boolean result = false;
        String query = String.format("UPDATE course SET course_description='%s' WHERE courseId='%s';", descrip, courseId);
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            int n = stmt.executeUpdate(query);

            // Query result processing:
            if (n > 0) { result = true; }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }
        return result;
    }
    
    /* Add a request record to table requestList. It is called when a student ask for a consent to register a course. */
    boolean addRequest(String studentId, String courseId) {
        boolean result = false;
        String query = String.format("INSERT INTO requestList VALUES ('%s', '%s');", studentId, courseId);
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            int n = stmt.executeUpdate(query);

            // Query result processing:
            if (n > 0) { result = true; }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    /* Query all the requests */
    List<String> getRequests(String instructorId) {
        List<String> requests = new LinkedList<>();
        String query = String.format("SELECT studentId, name as student_name, course_name FROM " + 
            "(SELECT studentId, course_name FROM requestList NATURAL JOIN course WHERE instructorId='%s') as r " + 
            "JOIN user ON r.studentId=user.id;", instructorId);
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            // Query result processing:
            while (rs.next()) {
                requests.add(rs.getString("studentId") + "\t" + rs.getString("student_name") + "\t" + rs.getString("course_name") + "\n");
            }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }
        return requests;
    }

    /* Delete the record in table requestList. */
    boolean processRequest(String studentId, String courseId, boolean consent) {
        boolean result = false;
        String query = String.format("DELETE FROM requestList WHERE studentId='%s' AND courseId='%s';", studentId, courseId);
        //System.out.println("The query is:\n" + query);
        try {
            conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
            stmt = conn.createStatement();
            int n = stmt.executeUpdate(query);

            // Query result processing:
            if (n > 0) { result = true; }

            // Tear down the connection:
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) { e.printStackTrace(); }

        if (result) {
            // Notify the instructor consent to the student. (This part is not implemented.) 
            System.out.printf("Student request result:\nstudentId: %s, courseId: %s, result: ", studentId, courseId);
            if (consent) { System.out.println("consented"); }
            else { System.out.println("rejected"); }
        }
        else {
            System.out.println("Cannot delete the record from database: the request record doesn't exist.");
        }
        return result;
    }
}
