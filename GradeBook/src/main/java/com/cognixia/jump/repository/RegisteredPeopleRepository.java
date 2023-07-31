package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.RegisteredPeople;

@Repository
public interface RegisteredPeopleRepository extends JpaRepository<RegisteredPeople, Integer>{

}
