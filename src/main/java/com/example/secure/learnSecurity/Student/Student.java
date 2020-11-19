package com.example.secure.learnSecurity.Student;

public class Student {

	private final Integer studentId;
	private final String studentName;
	public Student(Integer studentId, String studentName) {
		super();
		this.studentId = studentId;
		this.studentName = studentName;
	}
	public Integer getStudentId() {
		return studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	
	
}
