package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Course;
import com.cognixia.jump.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer>{

	@Query(value = "SELECT * from schedule WHERE student_id = ?1", nativeQuery = true)
	public List<Schedule> findByStudent(Integer id);

	@Query(value = "SELECT * from schedule WHERE course_id = ?1", nativeQuery = true)
	public List<Schedule> findByCourse(int intValue);

	@Query(value = "DELETE from schedule WHERE course_id = ?1", nativeQuery = true)
	public List<Schedule> deleteScheduleByCourse(Integer id);

}
