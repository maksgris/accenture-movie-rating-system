package com.mgs.library.business.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "userId")
    private List<Review> reviewIds;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_type_id")
    private UserType userType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userId", cascade = CascadeType.REMOVE)
    List<ReviewLike> reviewLikes;

    public User(Long id) {
        this.id = id;
    }
}
