package com.mgs.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UIMovie {
    private Long id;
    private String title;
    private String description;
    private String movieType;
    private Integer score;
    private String topReviewText;
    private int likes;
    private List<String> likedBy;

}
