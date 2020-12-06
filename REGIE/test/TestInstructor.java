package REGIE.test;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;

import java.util.*;
import REGIE.main.*;

// Test class of Instructor 
public class TestInstructor {
    Instructor i1, i2;

    public TestInstructor() {
        // Instantiate a student object, by logging into the system. 
        i1 = (Instructor) LoginEngine.login("i244653", "noahpsd1123");
        i2 = (Instructor) LoginEngine.login("i912234", "sofia8244");
    }

    @Test 
    public void assignGradeTest() {
        // Test method assignGrade
        System.out.printf("\n---------------------------------------------------\nTest: assign grade\n");
        System.out.println("Assign grade:");
        Assert.assertTrue(i1.assignGrade("MATH28555", "133227", "C"));
        Assert.assertTrue(i1.assignGrade("MATH28555", "133228", "F"));
        // Verify that the grades has been added.
        System.out.print(i1.reportGrades("MATH28555"));

        System.out.println("Modify grade:");
        Assert.assertTrue(i1.assignGrade("MATH28555", "133228", "D"));
        System.out.print(i1.reportGrades("MATH28555"));
    }

    @Test 
    public void reportGradesTest() {
        // Test method reportGrades
        System.out.printf("\n---------------------------------------------------\nTest: report grades\n");
        System.out.print(i2.reportGrades("CMSC62455"));
    }

    @Test 
    public void editCourseDescriptionTest() {
        // Test method editCourseDescription
        System.out.printf("\n---------------------------------------------------\nTest: edit course description\n");
        Course c = i2.getCourseInfo("CMSC63457");
        System.out.printf("Course description of %s before edition: %s\n", c.courseName, c.description);

        Assert.assertTrue(i2.editCourseDescription("CMSC63457", "Test of editing course description."));

        c = i2.getCourseInfo("CMSC63457");
        System.out.printf("Course description of %s after edition: %s\n", c.courseName, c.description);
    }

    @Test 
    public void viewRequestsTest() {
        // Test method viewRequests
        System.out.printf("\n---------------------------------------------------\nTest: view requests\n");
        System.out.print(i1.viewRequests());
    }

    @Test 
    public void replyRequestTest() {
        // Test method replyRequest
        System.out.printf("\n---------------------------------------------------\nTest: reply request\n");
        System.out.printf("Requests to instructor %s before replying:\n", i2.name);
        System.out.print(i2.viewRequests());

        Assert.assertTrue(i2.replyRequest("133227", "CMSC62456", true));
        Assert.assertTrue(i2.replyRequest("133228", "CMSC62456", false));

        System.out.printf("Requests to instructor %s after replying:\n", i2.name);
        System.out.print(i2.viewRequests());
    }
}