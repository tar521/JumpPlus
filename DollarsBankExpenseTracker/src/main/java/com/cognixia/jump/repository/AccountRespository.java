package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Account;

@Repository
public interface AccountRespository extends JpaRepository<Account, Integer> {

}
