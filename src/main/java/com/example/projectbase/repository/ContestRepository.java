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

    @Query("SELECT c FROM Contest c where c.title like CONCAT('%', :keyword, '%')")
    Page<Contest> findByTitle(@Param("keyword") String keyword, Pageable pageable);


    @Query("""
           SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END
           FROM Contest c JOIN c.participants u
           WHERE c.contestId = :contestId AND u.username = :username
            """)
    boolean existsParticipant(@Param("contestId") Long contestId,
                              @Param("username") String username);

    Page<Contest> findAll(Pageable pageable);
}
