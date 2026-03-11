package com.login.entity;

import com.login.model.UserRoleId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@NoArgsConstructor
@IdClass(UserRoleId.class)
public class UserRoleEntity {

    @Id
    @Column(nullable = false)
    private String username;

    @Id
    @Column(nullable = false)
    private String role;

    @Column(name="granted_date",nullable = false)
    private LocalDateTime grantedDate;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name="username", referencedColumnName = "username")
    private UserEntity user;

    public UserRoleEntity(UserEntity username, String role) {
        this.user = username;
        this.role = role;
        this.grantedDate = LocalDateTime.now();
    }
}
