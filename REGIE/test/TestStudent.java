package REGIE.test;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;

import java.util.*;
import REGIE.main.*;

// Test class of Student. It also tests the methods of abstract base class User. 
public class TestStudent {
    Student s1, s2;

    public TestStudent() {
        // Instantiate 2 student objects, by logging into the system. 
        s1 = (Student) LoginEngine.login("133224", "abc12345");
        s2 = (Student) LoginEngine.login("133225", "cccd5523");
    }

    @Test 
    public void registerTest() {
        // Test method registerCourse
        s1 = (Student) LoginEngine.login("133224", "abc12345");
        s2 = (Student) LoginEngine.login("133225", "cccd5523");

        System.out.printf("\n---------------------------------------------------\nTest: register courses\n");
        System.out.printf("Testing student: %s, id: %s\n", s1.name, s1.id);
        Assert.assertFalse(s1.registerCourse("MATH28556"));    // This should fail, since student s1 has already registered 3 courses. 
        
        System.out.printf("Testing student: %s, id: %s\n", s2.name, s2.id);
        Assert.assertFalse(s2.registerCourse("MATH28555"));    // This should fail, since student s2 doesn't fulfill the prerequisite requirements of the course.
        Assert.assertFalse(s2.registerCourse("CMSC62456"));    // This should fail, since student s2 is currently taking this course.
        Assert.assertTrue(s2.registerCourse("MATH28560"));     // This should succeed. 

        // Verify that the number of registered student of course "MATH28560" is incremented by 1.
        Course c1 = s2.getCourseInfo("MATH28560");
        Assert.assertEquals(c1.regNum, 37);      // original number is 36

        System.out.printf("Student %s has registered courses:\n", s2.name);
        for (Course c2: s2.involvedCourses()) { c2.display_info(); }
    }

    @Test 
    public void dropTest() {
        // Test method dropCourse
        // Test method registerCourse
        System.out.printf("\n---------------------------------------------------\nTest: drop courses\n");
        Student s = (Student) LoginEngine.login("133229", "bucher523");
        System.out.printf("Student %s has registered courses:\n", s.name);
        for (Course c: s.involvedCourses()) { c.display_info(); }

        Assert.assertFalse(s.dropCourse("MATH28554"));  // This should fail because the student doesn't register this course.
        Assert.assertTrue(s.dropCourse("MATH28553"));

        System.out.printf("Student %s has registered courses:\n", s.name);
        for (Course c: s.involvedCourses()) { c.display_info(); }

        // Verify that the number of registered student of course "MATH28553" is decremented by 1.
        Course c = s.getCourseInfo("MATH28553");
        Assert.assertEquals(c.regNum, 21);      // original number is 22
    }

    @Test 
    public void viewGradeTest() {
        // Test method viewGrade
        System.out.printf("\n---------------------------------------------------\nTest: get course grade\n");
        System.out.println(s1.viewGrade("MATH28554"));
    }

    @Test 
    public void viewTranscriptTest() {
        // Test method viewTranscript
        System.out.printf("\n---------------------------------------------------\nTest: view transcript\n");
        System.out.print(s1.viewTranscript());
    }

    @Test 
    public void askConsentTest() {
        // Test method askConsent
        System.out.printf("\n---------------------------------------------------\nTest: ask consent\n");
        Assert.assertTrue(s1.askConsent("MATH28570"));

        // Check that the instructor Donny Clark can view the new request
        Instructor i = (Instructor) LoginEngine.login("i235667", "don118244");
        System.out.print(i.viewRequests());
    }
}