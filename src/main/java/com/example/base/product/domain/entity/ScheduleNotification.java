package com.example.base.product.domain.entity;

import com.example.base.product.domain.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="schedule_notification")
@Builder

public class ScheduleNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name="schedule_date")
    private LocalDateTime scheduleDate;

    @Column(name="create_at")
    private LocalDateTime createAt;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

}
