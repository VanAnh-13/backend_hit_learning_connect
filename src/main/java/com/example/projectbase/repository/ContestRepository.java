package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Contest;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long > {
    Page<Contest> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("""
           SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END
           FROM Contest c JOIN c.participants u
           WHERE c.contestId = :contestId AND u.username = :username
            """)
    boolean existsParticipant(@Param("contestId") Long contestId,
                              @Param("username") String username);
}
