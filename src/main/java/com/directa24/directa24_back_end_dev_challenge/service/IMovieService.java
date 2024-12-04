package com.directa24.directa24_back_end_dev_challenge.service;

import com.directa24.directa24_back_end_dev_challenge.model.MovieResponse;

import java.util.List;


public interface IMovieService {
    /**
     * Retrieves a list of directors with movie counts greater than the specified threshold.
     *
     * @param threshold the minimum number of movies a director must have directed.
     * @return a list of directors' names in alphabetical order.
     */
    List<String> getDirectors(int threshold);

    /**
     * Endpoint to get a list of movies based on a given threshold.
     *
     * @param page the threshold value used for filtering movies.
     * @return a MovieResponse object containing the list of movies.
     */
    MovieResponse getMovies(int page);
}