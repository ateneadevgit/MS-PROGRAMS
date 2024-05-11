package com.fusm.programs.repository;

import com.fusm.programs.entity.Traceability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITraceabilityRepository extends JpaRepository<Traceability, Integer> {
}
