package com.mgs.library.business.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movieId", cascade = CascadeType.REMOVE)
    private List<Review> reviewIds;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movieId", cascade = CascadeType.REMOVE)
    private List<MovieLike> movieLikes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_type")
    private MovieType movieType;

    public Movie(Long id) {
        this.id = id;
    }
}
