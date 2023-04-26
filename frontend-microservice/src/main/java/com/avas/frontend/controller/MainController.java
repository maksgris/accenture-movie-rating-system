package com.avas.frontend.controller;


import com.avas.frontend.business.mappers.UIMovieMapper;
import com.avas.frontend.controller.feign.MovieMicroserviceProxy;
import com.avas.frontend.controller.feign.ReviewMicroserviceProxy;
import com.avas.frontend.controller.feign.UserLikeMicroserviceProxy;
import com.avas.frontend.model.UIMovie;
import com.avas.library.model.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/fe")
public class MainController {

    @Autowired
    private MovieMicroserviceProxy movieMicroserviceProxy;
    @Autowired
    private ReviewMicroserviceProxy reviewMicroserviceProxy;
    @Autowired
    private UserLikeMicroserviceProxy userLikeMicroserviceProxy;

    private final UIMovieMapper uiMovieMapper;

    public MainController(UIMovieMapper uiMovieMapper) {
        this.uiMovieMapper = uiMovieMapper;
    }

    @GetMapping("/index")
    public String showUserList(Model model) {
        List<MovieDTO> allMovies = movieMicroserviceProxy.getMovies();

        List<UIMovie> allMoviesUI = allMovies.stream()
                .map(t -> uiMovieMapper.mapToUIMovie(t, reviewMicroserviceProxy.getTopReview(t.getId()),
                        userLikeMicroserviceProxy.getLikesForAMovie(t.getId())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        model.addAttribute("allMoviesUI", allMoviesUI);
        return "index";
    }
}
