package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<ClassRoom,Long> {

}
