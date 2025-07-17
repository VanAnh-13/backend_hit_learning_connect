package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long > {
    Page<Contest> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
