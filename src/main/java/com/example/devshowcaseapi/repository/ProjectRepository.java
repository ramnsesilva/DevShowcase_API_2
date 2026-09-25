package com.example.devshowcaseapi.repository;

import com.example.devshowcaseapi.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN p.technologies t " +
            "WHERE (:technology IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :technology, '%')))")
    Page<Project> findByTechnology(@Param("technology") String technology, Pageable pageable);
}