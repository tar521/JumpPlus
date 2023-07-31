package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>{

	@Query(value = "SELECT * from course where teacher_id = ?1", nativeQuery = true)
	public List<Course> findByTeacher(Integer id);

	@Query(value = "SELECT * from course where teacher_id IS NULL", nativeQuery = true)
	public List<Course> findCourseList();

}
