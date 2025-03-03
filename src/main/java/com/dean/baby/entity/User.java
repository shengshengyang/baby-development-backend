package com.dean.baby.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Column(nullable = false, unique = true)
    private String email; // 用於註冊與驗證

    @OneToMany(mappedBy = "user")
    private List<Baby> babies;

    @PrePersist
    public void prePersist() {
        if (this.username == null || this.username.isEmpty()) {
            this.username = this.email;
        }
    }
}
