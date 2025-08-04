package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Contest;
import com.example.projectbase.domain.entity.ContestSubmission;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestSubmissionRepository extends JpaRepository<ContestSubmission, Long> {

    Optional<ContestSubmission> findByContest_ContestIdAndCreatedBy_Username(Long contestId, String username);

    boolean existsByContest_ContestIdAndCreatedBy_Username(Long contestId, String username);

//    @Query("SELECT c FROM Contest c LEFT JOIN FETCH c.submissions WHERE c.contestId = :id")
//    Optional<Contest> findByIdWithSubmissions(@Param("id") Long id);

    @Query("SELECT c FROM ContestSubmission c WHERE c.createdBy.id = :createdById AND c.contest.contestId = :contestContestId")
    Page<ContestSubmission> findAllSubmission(@Param("contestContestId") Long contestContestId, @Param("createdById") Long createdById, Pageable pageable);


}
