package com.test.ecommerceorderservice.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "idx_users_email", columnList = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SQLDelete(sql = "UPDATE users SET delete_at = NOW() WHERE id = ?")
@SQLRestriction(value = "delete_at IS NULL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToOne(optional = false, targetEntity = RoleEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    private String token;

    private LocalDateTime tokenExpiration;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private LocalDateTime deleteAt;
}