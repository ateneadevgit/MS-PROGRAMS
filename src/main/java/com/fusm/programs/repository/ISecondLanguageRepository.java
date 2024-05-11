package com.fusm.programs.repository;

import com.fusm.programs.entity.SecondLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISecondLanguageRepository extends JpaRepository<SecondLanguage, Integer> {

    @Query(
            value = "select * " +
                    "from second_language where " +
                    "enabled = true " +
                    "order by created_at desc ",
            nativeQuery = true
    )
    List<SecondLanguage> findAllOrdered();

    Optional<SecondLanguage> findBySecondLanguageIdAndEnabled(
            Integer secondLanguageId, Boolean enabled
    );

}
