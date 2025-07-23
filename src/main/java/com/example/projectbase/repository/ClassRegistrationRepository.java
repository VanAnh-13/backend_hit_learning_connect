package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.ClassRegistration;
import com.example.projectbase.domain.entity.ClassRoom;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.model.RegistrationStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRegistrationRepository extends JpaRepository<ClassRegistration, Long> {

    Optional<ClassRegistration> findByClassEntityAndStudent(ClassRoom classRoom, User student);
    List<ClassRegistration> findByStudent(User student);
    List<ClassRegistration> findByClassEntity(ClassRoom classRoom);
    boolean existsByClassEntityAndStudent(ClassRoom classRoom, User student);
    Page<ClassRegistration> findByStudent(User student, Pageable pageable);

    @Query("SELECT r FROM ClassRegistration r WHERE " +
            "(:classId IS NULL OR r.classEntity.classId = :classId)")
    Page<ClassRegistration> filterRegistrations(@Param("classId") Long classId,
                                           Pageable pageable);

    @Query("SELECT r FROM ClassRegistration r " +
            "WHERE r.student.id = :userId " +
            "  AND r.status = :status")
    Page<ClassRegistration> findByStudentIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") RegistrationStatus status,
            Pageable pageable);
}
