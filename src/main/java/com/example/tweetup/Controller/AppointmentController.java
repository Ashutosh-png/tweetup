package com.example.tweetup.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tweetup.Services.AppointmentService;
import com.example.tweetup.Services.UserDetailsServiceImpl;
import com.example.tweetup.User.Appointment;
import com.example.tweetup.User.User;
import com.example.tweetup.User.User.Role;

@Controller
public class AppointmentController {
	
	@Autowired
	AppointmentService aser;
	
	
	@Autowired
	UserDetailsServiceImpl ser;
	
	@GetMapping("/patient/bookAppointment")
	public String showdoc(Model model) {
		

		List<User> doctoruser = ser.findUserByRole("DOCTOR");
		model.addAttribute("doctoruser", doctoruser);
		return "Appointmentpage";
	}
	
	@PostMapping("/patient/bookAppointment")
	public String bookAppointment(@RequestParam("doctorUsername") String doctorUsername,
	                              @RequestParam("appointmentTime") LocalDateTime appointmentTime,
	                              Principal principal) {
	    User patient = ser.getByUsername(principal.getName());
	    User doctor = ser.getByUsername(doctorUsername);

	    Appointment appointment = new Appointment();
	    appointment.setAppointmentTime(appointmentTime);
	    appointment.setDoctorId(doctor.getId());
	    appointment.setPatientId(patient.getId());
	    appointment.setDocname(doctor.getUsername());
	    appointment.setPatname(patient.getUsername());
	    aser.save(appointment);

	    return "redirect:/patient/appointments";
	}

	
	@GetMapping("/patient/appointments")
	public String showAppointments(Model model, Principal principal) {
	    User user = ser.getByUsername(principal.getName());
	    List<Appointment> appointments = aser.getAppointmentsByPatientId(user.getId());
	    model.addAttribute("appointments", appointments);
	    return "patient-appointments";
	}
	
	@GetMapping("/doctor/appointments")
	public String showDoctorAppointments(Model model, Principal principal) {
	    User doctor = ser.getByUsername(principal.getName());
	    List<Appointment> appointments = aser.getAppointmentsByDoctorId(doctor.getId());
	    model.addAttribute("appointments", appointments);
	    return "doctor-appointments";
	}
	
	@PostMapping("doctor/deleteAppointment")
	public String deleteAppointment(@RequestParam("id") Long id) {
		Appointment appointment = aser.findById(id);
		aser.deleteAppointment(appointment);
		    
		    return "redirect:/doctor/appointments";
	
	}
	
	@PostMapping("patient/deleteAppointment")
	public String patientdeleteAppointment(@RequestParam("id") Long id) {
		Appointment appointment = aser.findById(id);
		aser.deleteAppointment(appointment);
		    
		    return "redirect:/patient/appointments";
	
	}
	
	@GetMapping("/admin/appointments")
	public String adminappointment(Model model) {
		List<Appointment> appointment = aser.showall();
		model.addAttribute("appointment", appointment);
		return "admin-appointment";
	}



}
