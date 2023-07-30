package com.ChemiChat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @NotNull
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> members;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Message> history = new ArrayList<>();
}
