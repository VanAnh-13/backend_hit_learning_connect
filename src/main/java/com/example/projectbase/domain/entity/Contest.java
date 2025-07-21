package com.example.projectbase.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "contests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contest_id")
    private Long contestId;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name="highestScore")
    private double highestScore;

    @Column(name="result_Summary", length = 1000)
    private String resultSummary;

    @Column(name = "ranking")
    private String ranking;

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContestProblem> contestProblems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "contest",fetch = FetchType.LAZY)
    private List<ContestSubmission> submissions;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "contest_paticipants",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();
}
