package com.mgs.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieLikeDTO {

    private Long id;
    private Long userId;
    private Long movieId;
}
