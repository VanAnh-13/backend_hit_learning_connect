package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.ClassRegistration;
import com.example.projectbase.domain.entity.ClassRoom;
import com.example.projectbase.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRegistrationRepository extends JpaRepository<ClassRegistration, Long> {

    Optional<ClassRegistration> findByClassEntityAndStident(ClassRoom classRoom, User student);
    List<ClassRegistration> findByStudent(User student);
    List<ClassRegistration> findByClassEntity(ClassRoom classRoom);
    boolean existsByClassEntityAndStudent(ClassRoom classRoom, User student);
}
