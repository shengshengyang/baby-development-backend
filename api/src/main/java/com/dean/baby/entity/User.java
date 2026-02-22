package com.dean.baby.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @PrePersist
    public void prePersist() {
        if (this.username == null || this.username.isEmpty()) {
            this.username = this.email;
        }
    }

    public boolean isBaby(UUID babyId) {
        return this.babies.stream().anyMatch(baby -> baby.getId().equals(babyId));
    }

    public boolean hasRole(String roleName) {
        return this.roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public boolean isUser() {
        return hasRole("ROLE_USER");
    }
}
