package com.cognixia.jump.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.jump.model.Course;
import com.cognixia.jump.model.Grade;
import com.cognixia.jump.model.Schedule;
import com.cognixia.jump.model.Student;
import com.cognixia.jump.model.Teacher;
import com.cognixia.jump.repository.GradeRepository;

@Service
public class GradeService {
	
	@Autowired
	private GradeRepository repo;
	
	public List<Grade> loadGradesStudent(Student student) {
		return repo.findGradeByStudent(student.getId());
	}
	
	public List<Grade> loadGradesTeacher(Teacher teacher) {
		return repo.findGradeByTeacher(teacher.getId());
	}
	
	public List<Grade> loadGradesByCourse(Course course) {
		return repo.findGradeByCourse(course.getId());
	}

	public List<Grade> loadGradesByCourseAndStudent(Schedule s, Student added) {
		
		return repo.findGradeByCourseAndStudent(s.getCourse().getId(), added.getId());
	}

	public void removeStudentGradesForCourse(Course found, Student removed) {
		List<Grade> deleteList = repo.findGradeByCourseAndStudent(found.getId(), removed.getId());
		if (!deleteList.isEmpty()) {
			repo.deleteGradeByCourseAndStudent(found.getId(), removed.getId());
		}
	}
	
	public Grade enterGrade(Grade grade) {
		grade.setId(null);
		return repo.save(grade);
	}

	public void removeGradesForCourse(Course c) {
		List<Grade> deleteGrades = repo.findGradeByCourse(c.getId());
		if (!deleteGrades.isEmpty()) {
			repo.deleteGradeByCourse(c.getId());
		}
	}

}
