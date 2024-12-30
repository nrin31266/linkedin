package com.linkedin.backend.features.authentication.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true, nullable = false)
    String email;
    @Column(nullable = false)
    Boolean emailVerified;
    String emailVerificationToken;
    Date emailVerificationTokenExpiryDate;
    @JsonIgnore
    String password;
    private String passwordResetToken;
    private Date passwordResetTokenExpiryDate;

    @PrePersist
    public void prePersist() {
        emailVerified = false;
    }
}
