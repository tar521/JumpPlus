package com.cognixia.jump.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.jump.model.Course;
import com.cognixia.jump.model.Teacher;
import com.cognixia.jump.repository.CourseRepository;

@Service
public class CourseService {
	
	@Autowired
	private CourseRepository repo;
	
	public List<Course> loadCourses(Teacher teacher) {
		
		return repo.findByTeacher(teacher.getId());
	}
	
	public List<Course> getCourseList() {
		return repo.findCourseList();
	}
	
	public Course addCourse(Course course) {
		return repo.save(course);
	}
	
	public void removeCourse(Course course) {
		repo.delete(course);
	}

}
