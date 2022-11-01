package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentListDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

@SpringBootTest
public class EndToEndTestNewAssignment {
	// do to your own local file location
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/Users/cueva/OneDrive/Desktop/chromedriver_win32/chromedriver.exe";
	// change to your own
	public static final String URL = "http://localhost:3000";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	public static final String TEST_ASSIGNMENT_NAME = "Test Assignment 1";
	public static final String TEST_COURSE_TITLE = "Test Course 1";
	public static final String TEST_STUDENT_NAME = "Test Student 1";
	public static final String TEST_COURSE_ID_STRING = "30157";
	public static final int TEST_COURSE_ID = 30157;
	public static final String TEST_DUE_DATE = "2021-10-17";

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentGradeRepository assignnmentGradeRepository;

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Test
	public void newAssignmentTest() throws Exception {

//		Database setup:  create course		
		Course c = new Course();
		c.setCourse_id(TEST_COURSE_ID);
		c.setInstructor(TEST_INSTRUCTOR_EMAIL);
		c.setSemester("Fall");
		c.setYear(2021);
		c.setTitle(TEST_COURSE_TITLE);


//	    add a student TEST into course 
		Enrollment e = new Enrollment();
		e.setCourse(c);
		e.setStudentEmail(TEST_USER_EMAIL);
		e.setStudentName(TEST_STUDENT_NAME);

		c = courseRepository.save(c);
		e = enrollmentRepository.save(e);

		AssignmentGrade ag = null;
		
		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on
				
		/*
		 	* initialize the WebDriver and get the home page. 
		*/
		// change to your own 
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		// try method to add an assignment
		// get the div class to see whether you go the assignment or not
		
		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);
		
		try {
			// Click the add assignment link
			driver.findElement(By.xpath("//a[@id='addAssignment']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Assignment Name
			WebElement elements = driver.findElement(By.xpath("//input[@type='text' and @name='assignmentName']"));
			elements.sendKeys(TEST_ASSIGNMENT_NAME);

			// Assignment DueDate
			elements  = driver.findElement(By.xpath("//input[@type='text' and @name='dueDate']"));
			elements.sendKeys(TEST_DUE_DATE);
			
			// Assignment CourseId
			elements  = driver.findElement(By.xpath("//input[@type='text' and @name='courseId']"));
			elements.sendKeys(TEST_COURSE_ID_STRING);
			
			// Click Add assignment link 
			driver.findElement(By.xpath("//button[@id='createAssignment']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Press Go back Button 
			driver.findElement(By.xpath("//a[@id='goBack']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Verify assignment was added
			List<WebElement> element  = driver.findElements(By.xpath("//div[@data-field='assignmentName']/div"));
			boolean founds = false;
			for (WebElement we : element) {
				System.out.println(we.getText()); // for debug
				if (we.getText().equals(TEST_ASSIGNMENT_NAME)) {
					founds=true;
					System.out.println("found");
					we.findElement(By.xpath("descendant::input")).click();
					break;
				}
			}
			assertTrue(founds, "Unable to locate TEST ASSIGNMENT 1 in list of assignments to be graded.");
			
		}catch(Exception ex) {
			throw ex;
		}
	}
}
