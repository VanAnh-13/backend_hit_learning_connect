package com.hit.leaning_connect.repository;

import com.hit.leaning_connect.domain.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Class, Long> {
}
