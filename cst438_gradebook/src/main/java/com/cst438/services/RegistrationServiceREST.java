package com.cst438.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.CourseDTOG;
import com.cst438.domain.CourseDTOG.GradeDTO;

public class RegistrationServiceREST extends RegistrationService {

	
	RestTemplate restTemplate = new RestTemplate();
	
	@Value("${registration.url}") 
	String registration_url;
	
	public RegistrationServiceREST() {
		System.out.println("REST registration service ");
	}
	
	@Override
	public void sendFinalGrades(int course_id , CourseDTOG courseDTO) { 		
		// When teacher does POST to /course/{course_id}/finalgrades 
		// send final grades of all students to Registration backend using CourseDTOG
		System.out.println("Sending final grade " + course_id + " "+ courseDTO);
		restTemplate.put(registration_url+"/course/"+course_id, courseDTO);
		System.out.println("After sending final grade");
		
	}
}
