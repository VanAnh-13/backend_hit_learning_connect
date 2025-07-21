package com.example.projectbase.domain.dto.response.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassResponseDto{
        Long classId;
        String title;
        String description;
        Long teacherId;
        String teacherUsername;
        String teacherFullName;
        String status;
        LocalDate startDate;
        LocalDate endDate;
}

