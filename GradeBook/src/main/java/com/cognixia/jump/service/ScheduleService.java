package com.cognixia.jump.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.jump.model.Course;
import com.cognixia.jump.model.Schedule;
import com.cognixia.jump.model.Student;
import com.cognixia.jump.repository.ScheduleRepository;

@Service
public class ScheduleService {

	@Autowired
	private ScheduleRepository repo;
	
	public List<Schedule> loadSchedule(Student student) {
		
		return repo.findByStudent(student.getId());
	}
	
	public List<Schedule> getScheduleByCourse(Course course) {
		return repo.findByCourse(course.getId().intValue());
	}
	
	public Schedule addSchedule(Schedule schedule) {
		return repo.save(schedule);
	}

	public void removeSchedule(Schedule s) {
		repo.delete(s);
		
	}

	public void removeScheduleByCourse(Course c) {
		List<Schedule> deleteList = repo.findByCourse(c.getId());
		if (!deleteList.isEmpty()) {
			repo.deleteScheduleByCourse(c.getId());
		}
	}
}
