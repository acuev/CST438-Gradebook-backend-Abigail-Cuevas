package com.cst438.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentListDTO;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;

@RestController
public class EnrollmentController {

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;

	/*
	 * endpoint used by registration service to add an enrollment to an existing
	 * course.
	 */
	@PostMapping("/enrollment")
	@Transactional
	public EnrollmentDTO addEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
		//Receive the EnrollmentDTO and update enrollment table.
        Course c = courseRepository.findById(enrollmentDTO.course_id).orElse(null);
		
		if (c == null) {
			throw new ResponseStatusException( HttpStatus.UNAUTHORIZED, "Course not found.");
		}
		Enrollment enroll = new Enrollment();
		enroll.setStudentEmail(enrollmentDTO.studentEmail);
		enroll.setStudentName(enrollmentDTO.studentName);
		enroll.setCourse(c);
		
		enroll = enrollmentRepository.save(enroll);
		
		enrollmentDTO.id = enroll.getId();
		
		return enrollmentDTO;
		
	}

}
