package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer>{

	@Query(value = "SELECT * from grade WHERE student_id = ?1", nativeQuery = true)
	public List<Grade> findGradeByStudent(Integer id);

	@Query(value = "SELECT * from grade WHERE teacher_id = ?1", nativeQuery = true)
	public List<Grade> findGradeByTeacher(Integer id);

	@Query(value = "SELECT * from grade WHERE course_id = ?1", nativeQuery = true)
	public List<Grade> findGradeByCourse(Integer id);

	@Query(value = "SELECT * from grade WHERE course_id = ?1 AND student_id = ?2", nativeQuery = true)
	public List<Grade> findGradeByCourseAndStudent(Integer id, Integer id2);
	
	@Query(value = "DELETE FROM grade WHERE course_id = ?1 AND student_id = ?2", nativeQuery = true)
	public void deleteGradeByCourseAndStudent(Integer id, Integer id2);

	@Query(value = "DELETE FROM grade WHERE course_id = ?1", nativeQuery = true)
	public void deleteGradeByCourse(Integer id);

}
