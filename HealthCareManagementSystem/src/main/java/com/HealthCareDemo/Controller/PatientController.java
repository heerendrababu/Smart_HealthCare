package com.HealthCareDemo.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.HealthCareDemo.dao.PatientDao;
import com.HealthCareDemo.model.Patient;

@Controller
public class PatientController {
	@RequestMapping("registerPatient")
	public ModelAndView insertPatientDetails(HttpServletRequest request) {
		// Check PatientEmailExists
		String checkEmail = request.getParameter("email");

		// Check if email exists before proceeding with registration
		boolean emailExists = PatientDao.checkPatientEmailExists(checkEmail);

		// Creating ModelAndView to return the result to the JSP
		ModelAndView mv = new ModelAndView();
		if (emailExists) {
			mv.addObject("checkMail", "Email already exists!");
			mv.setViewName("PatientRegistration.jsp");
			return mv; // Return early if email exists
		}
		String name = request.getParameter("pname");
		int age = Integer.parseInt(request.getParameter("age"));
		String bloodGroup = request.getParameter("bloodGroup");
		String email = request.getParameter("email");
		long mobile = Long.parseLong(request.getParameter("mobile"));
		String password = request.getParameter("password");

		// The controller collects the data, creating object for model class(Patient),
		// it represents the structure of the data you're working with
		// and stores the data inside this object.
		Patient p = new Patient(name, age, bloodGroup, email, mobile, password);

		// calling patientDao method and insert data into that method
		PatientDao.insertPatientDetails(p);

		mv.addObject("message", "Patient registered successfully.");
		mv.addObject("showBackToLogin", true); // Adding flag to display "Back to Login" button
		mv.setViewName("PatientRegistration.jsp");

		return mv;

	}

	// checking Patient login credentials if yes re-direcing user to WelcomeUser.jsp
	// page

	@RequestMapping("patientlogin")
	public ModelAndView checkPatientLogin(@RequestParam("email") String email,
			@RequestParam("password") String password, HttpServletRequest request) {

		boolean isAuthenticated = PatientDao.checkPatientLogin(email, password); // Method to check login credentials

		// The ModelAndView object i.e "mv" holds both the view name and the model data
		ModelAndView mv = new ModelAndView();

		// If credentials are valid, redirect to dashboard; otherwise, return to login
		// with error
		 // If credentials are valid, redirect to dashboard; otherwise, return to login with error
        if (isAuthenticated) {
            // Get patient ID after successful login
            int patientId = PatientDao.getPatientId(email); // Method to retrieve patient ID based on email
            
            // Store patient ID in session
            HttpSession session = request.getSession();
            session.setAttribute("loggedInPatientId", patientId);
            
            mv.setViewName("WelcomeUser.jsp");
        }
		else
		{
			mv.addObject("message", "Invalid email or password!"); // Passing an error message
			mv.setViewName("Patient.jsp");
		}

		return mv;
	}



}
