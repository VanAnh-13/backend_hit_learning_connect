package com.example.projectbase.domain.entity;


import com.example.projectbase.domain.model.SubmissionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "contest_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long submissionId;

//    @ManyToOne
//    @JoinColumn(name = "problem_id", nullable = true)
//    @JsonIgnore
//    private ContestProblem problem;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = true)
//    @JsonIgnore
//    private User user;

    @Column(name = "code", nullable = true)
    @JsonIgnore
    private String code;

    @Column(name="highestScore")
    private double highestScore;

    @Column(name="result_Summary", length = 1000)
    private String resultSummary;

    @Column(name = "ranking")
    private String ranking;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "file_Url")
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true)
    @JsonIgnore
    private SubmissionStatus status = SubmissionStatus.PENDING;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();
}