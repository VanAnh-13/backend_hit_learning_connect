package com.example.base.product.domain.entity;

import com.example.base.product.domain.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
@Builder
@ToString(exclude = "password")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username", length = 40, nullable = false)
    private String username;

    @Column(name="birthday", length = 15, nullable = false)
    private String birthday;

    @Column(length = 15, nullable = false)
    private String password;

    @Column(length = 25, nullable = false)
    private String address;

    @Column(length = 14, nullable = false)
    private String phoneNumber;

    @Column(length = 5, nullable = false)
    private String gender;

    @Column(length = 25, nullable = false)
    private String email;

    @Column(length = 25, nullable = false)
    private String fullName;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private ClassRoom classRoom;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ScheduleNotification> notifications = new ArrayList<>();
}
