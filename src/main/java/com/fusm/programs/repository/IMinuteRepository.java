package com.fusm.programs.repository;

import com.fusm.programs.entity.Minute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMinuteRepository extends JpaRepository<Minute, Integer> {
}
