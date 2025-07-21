package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

//    @Query("SELECT c FROM Contest c WHERE (c.createdBy.username = :username OR c.isPublic = true) AND c.endTime > CURRENT_TIMESTAMP")
//    Page<Contest> findAvailableForUser(@Param("username") String username, Pageable pageable);

    Page<Contest> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

}
