package com.eh.stock.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    private String name;
    private String address;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;


    public static User of(String name, String address, UserStatus userStatus){
        return new User(name, address, userStatus);
    }

    private User(String name, String address, UserStatus userStatus) {
        this.name = name;
        this.address = address;
        this.userStatus = userStatus;
    }
}
