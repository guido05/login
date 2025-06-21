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

    @Column(name="granted_date",nullable = false,columnDefinition = "DATETIME")
    private LocalDateTime grantedDate;

    @ManyToOne
    @JoinColumn(name="username", referencedColumnName = "username", insertable = false, updatable = false)
    private UserEntity user;
}
