package com.example.ktcFinal.user;

import com.example.ktcFinal.BaseEntity;
import com.example.ktcFinal.eNum.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String nickname;
    private Integer age;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;

    private String profileImg;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
