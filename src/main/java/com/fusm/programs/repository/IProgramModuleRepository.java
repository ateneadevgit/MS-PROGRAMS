package com.fusm.programs.repository;

import com.fusm.programs.entity.ProgramModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProgramModuleRepository extends JpaRepository<ProgramModule, Integer> {

    @Query(
            value = "select * from program_module " +
                    "where is_enlarge = :isEnlarge " +
                    "and allow_edition = true " +
                    "order by module_order ",
            nativeQuery = true
    )
    List<ProgramModule> findAllByModules(@Param("isEnlarge") Boolean isEnlarge);

    @Query(
            value = "select * from program_module " +
                    "where is_enlarge = :isEnlarge " +
                    "order by module_order ",
            nativeQuery = true
    )
    List<ProgramModule> findAllByModulesNoEditables(@Param("isEnlarge") Boolean isEnlarge);

}
