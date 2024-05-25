package com.findme.secutiry.repository;

import com.findme.secutiry.model.SecurityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityRepository extends JpaRepository<SecurityEntity, Long> {
}
