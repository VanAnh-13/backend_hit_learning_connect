package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.ClassRoom;
import com.example.projectbase.domain.entity.Document;
import com.example.projectbase.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassRoom,Long> {

    @Query("""
        SELECT c
        FROM ClassRoom c
        WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<ClassRoom> searchByKeyword(
            String keyword,
            Pageable pageable
    );

    List<ClassRoom> findByTeacher(User teacher);
}
