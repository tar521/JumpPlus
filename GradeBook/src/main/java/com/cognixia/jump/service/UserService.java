package com.cognixia.jump.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.jump.model.Student;
import com.cognixia.jump.model.Teacher;
import com.cognixia.jump.repository.StudentRepository;
import com.cognixia.jump.repository.TeacherRepository;

@Service
public class UserService {
	
	@Autowired
	private TeacherRepository tRepo;
	
	@Autowired
	private StudentRepository sRepo;
	
	public void saveStudent(Student student) {
		sRepo.save(student);
	}
	
	public void saveTeacher(Teacher teacher) {
		tRepo.save(teacher);
	}

	public Student getStudentById(Student student) {
		Optional<Student> found = sRepo.findById(student.getId());
		if (found.isEmpty()) {
			return null;
		} else {
			return found.get();
		}
		
	}
	
	public Map<Integer, Student> getAllStudents() {
		List<Student> allStudents = sRepo.findAll();
		Map<Integer, Student> result = new ConcurrentHashMap<Integer, Student>();
		for (Student s : allStudents) {
			result.put(s.getId(), s);
		}
		return result;
	}

}
