package com.fusm.programs.repository;

import com.fusm.programs.entity.Modality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IModalityRepository extends JpaRepository<Modality, Integer> {
}
