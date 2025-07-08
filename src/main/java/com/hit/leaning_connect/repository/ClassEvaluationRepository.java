package com.hit.leaning_connect.repository;

import com.hit.leaning_connect.domain.entity.ClassEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassEvaluationRepository extends JpaRepository<ClassEvaluation, Long> {
    List<ClassEvaluation> findByClassObjClassId(Long classId);
    List<ClassEvaluation> findByUserUserId(Long userId);
}
