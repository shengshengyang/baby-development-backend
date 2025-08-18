package com.dean.baby.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @UuidGenerator
    private UUID id;

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

    public boolean isBaby(UUID babyId) {
        return this.babies.stream().anyMatch(baby -> baby.getId().equals(babyId));
    }
}
