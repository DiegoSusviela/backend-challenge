package com.directa24.directa24_back_end_dev_challenge.controller;

import com.directa24.directa24_back_end_dev_challenge.model.DirectorResponse;
import com.directa24.directa24_back_end_dev_challenge.model.MovieResponse;
import com.directa24.directa24_back_end_dev_challenge.service.IMovieService;
import com.directa24.directa24_back_end_dev_challenge.service.impl.MovieServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieController {
    private final IMovieService movieService;

    @Autowired
    public MovieController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    /**
     * Endpoint to get a list of directors with movie count greater than the given threshold.
     *
     * @param threshold the minimum number of movies a director must have to be included.
     * @return a JSON response with a list of directors.
     */
    @GetMapping("/directors")
    public ResponseEntity<?> getDirectors(@RequestParam("threshold") int threshold) {
        List<String> directors = movieService.getDirectors(threshold);
        if (directors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No directors found with more than " + threshold + " movies.");
        }
        return ResponseEntity.ok(new DirectorResponse(directors));
    }

    /**
     * Endpoint to get a list of movies based on a given threshold.
     *
     * @param page the threshold value used for filtering movies.
     * @return a MovieResponse object containing the list of movies.
     */
    @GetMapping("/movies")
    public MovieResponse getMovies(@RequestParam("page") int page) {
        return movieService.getMovies(page);
    }
}
