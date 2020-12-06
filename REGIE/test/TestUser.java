package REGIE.test;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;

import java.util.*;
import REGIE.main.*;

// Test class of all the methods in User class. Each method is tested in a Student class and an instructor class.  
public class TestUser {
    Student s;
    Instructor i;

    public TestUser() {
        // Instantiate 2 student objects, by logging into the system. 
        s = (Student) LoginEngine.login("133224", "abc12345");
        assert s.role == "student";

        i = (Instructor) LoginEngine.login("i244653", "noahpsd1123");
        assert i.role == "instructor";
    }

    @Test 
    public void browseCourseTest() {
        // Test method browseCourses. 
        System.out.printf("\n---------------------------------------------------\nTest: browse courses\n");
        System.out.println("Student class test:");
        List<Course> courses = s.browseCourses(null, "computer science", null, null, null);         // Search for courses that are from department of computer science. 
        for (Course c: courses) { 
            Assert.assertEquals(c.department, "computer science");
            c.display_info(); 
        }
        System.out.println("Instructor class test:");
        List<Course> courses2 = i.browseCourses(null, null, null, "math263", "Noah Oliver"); // Search for courses that are taught by Noah Oliver and are in room math263. 
        for (Course c: courses2) { 
            Assert.assertEquals(c.instrName, "Noah Oliver");
            Assert.assertEquals(c.room, "math263");
            c.display_info(); 
        }
    }

    @Test 
    public void involvedCourseTest() {
        // Test method involvedCourses. 
        System.out.printf("\n---------------------------------------------------\nTest: involved courses\n");
        System.out.println("Student class test:");
        List<Course> courses = s.involvedCourses();
        for (Course c: courses) { c.display_info(); }

        System.out.println("Instructor class test:");
        List<Course> courses2 = i.involvedCourses();
        for (Course c: courses2) { c.display_info(); }
    }

    @Test 
    public void CourseInfoTest() {
        // Test method getCourseInfo in class user. 
        System.out.printf("\n---------------------------------------------------\nTest: get course info\n");
        System.out.println("Student class test:");
        Course c = s.getCourseInfo("CMSC62456");
        Assert.assertTrue(c != null);
        Assert.assertEquals(c.courseId, "CMSC62456");
        c.display_info();

        System.out.println("Instructor class test:");
        Course c2 = i.getCourseInfo("PHY10020");     // Query a non-existing course. 
        Assert.assertTrue(c2 == null);
        System.out.printf("The course %s does not exist.\n", "PHY10020");
    }

    @Test 
    public void modifyAccountTest() {
        // Test method modifyAccount in class user. 
        System.out.printf("\n---------------------------------------------------\nTest: modify account\n");
        System.out.println("Student clas tests:");
        Assert.assertTrue(s.modifyAccount(null, null, null));      // Change nothing.
        Assert.assertTrue(s.modifyAccount(null, "abc13335", "economics"));
        User s2 = LoginEngine.login("133224", "abc13335");
        Assert.assertEquals(s2.department, "economics");
        Assert.assertTrue(s2.modifyAccount(null, "abc12345", null));       // Change the password back. 

        System.out.println("Instructor class test:");
        Assert.assertTrue(i.modifyAccount("Noooh Oliver", null, null));
        User i2 = LoginEngine.login("i244653", "noahpsd1123");
        Assert.assertEquals(i2.name, "Noooh Oliver");
        Assert.assertTrue(i2.modifyAccount("Noah Oliver", null, null));      // Change the name back. 
    }
}