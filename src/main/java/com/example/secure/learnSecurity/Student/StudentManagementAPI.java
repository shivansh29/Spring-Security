package com.example.secure.learnSecurity.Student;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementAPI {

	private final List<Student> students = Arrays.asList(
			new Student(1,"Shivansh"),
			new Student(2,"Satyam"),
			new Student(3,"PK")
			);
	
	@GetMapping
	public List<Student> getAll(){
		return students;
	}
	
	@PutMapping("/{studentId}")
	public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
		System.out.println(studentId+"   "+student);
	}
	
	
	@PostMapping
	public void registerStudent(Student student) {
		System.out.println("Registered Successfully");
	}
	
	@DeleteMapping("{studentId}")
	public void deleteStudent(@PathVariable("studentId") Integer studentId) {
		System.out.println("Deleted Successfully  "+studentId);
	}
}
