package com.mgs.library.business.repository.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(name = "text_review")
    private String textReview;
    private int score;
    @Column(name = "review_date")
    private LocalDate reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movieId;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reviewId", cascade = CascadeType.REMOVE)
    private List<ReviewLike> reviewLikes;

    public Review(Long id) {
        this.id = id;
    }
}
