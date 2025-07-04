package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<ClassRoom,Long> {

}
