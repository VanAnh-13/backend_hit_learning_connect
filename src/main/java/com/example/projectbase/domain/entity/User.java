package com.example.projectbase.domain.entity;

import com.example.projectbase.domain.entity.common.DateAuditing;
import com.example.projectbase.domain.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nimbusds.openid.connect.sdk.claims.Gender;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User extends DateAuditing {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(insertable = false, updatable = false, nullable = false)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  @Nationalized
  @Column(name = "full_name")
  private String fullName;

  @Column(name = "avatar_url")
  private String avatarUrl;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "gender")
  private String gender;

  @Column(name = "birthday")
  private LocalDate birthday;

  @Column(nullable = false)
  private LocalDateTime lastLogin;

  //Link to table Role
  @ManyToOne
  @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "FK_USER_ROLE"))
  private Role role;

  @ManyToOne
  @JoinColumn(name="class_id", foreignKey = @ForeignKey(name="FK_CLASS_ID"))
  private ClassRoom classRoom;

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  @JsonIgnore
  private Set<ClassRegistration> classRegistrations = new HashSet<>();
}
