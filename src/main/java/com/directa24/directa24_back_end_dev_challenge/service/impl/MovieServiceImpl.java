package com.directa24.directa24_back_end_dev_challenge.service.impl;

import com.directa24.directa24_back_end_dev_challenge.exception.MovieApiException;
import com.directa24.directa24_back_end_dev_challenge.model.MovieResponse;
import com.directa24.directa24_back_end_dev_challenge.service.IMovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements IMovieService {
    public static final String MOVIE_API_URL = "https://eron-movies.wiremockapi.cloud/api/movies/search?page=";
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);
    private static final int maxPages = 10;
    private final RestTemplate restTemplate;

    public MovieServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<String> getDirectors(int threshold) {
        Map<String, Integer> directorMovieCount = new HashMap<>();
        int currentPage = 1;
        int totalPages = maxPages;

        while (currentPage <= totalPages) {
            MovieResponse movieResponse = getMovies(currentPage);
            movieResponse.getData().forEach(movie -> directorMovieCount.merge(movie.getDirector(), 1, Integer::sum));

            totalPages =  movieResponse.getTotalPages();
            currentPage++;
        }

        return directorMovieCount.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }

    public MovieResponse getMovies(int page) {
        String url = MOVIE_API_URL + page;

        try {
            return restTemplate.getForObject(url, MovieResponse.class);
        } catch (HttpClientErrorException e) {
            // Handle 4xx errors
            logger.error("Client error while fetching movies: {}", e.getMessage());
            throw new MovieApiException("Client error occurred: " + e.getStatusText(), e);
        } catch (HttpServerErrorException e) {
            // Handle 5xx errors
            logger.error("Server error while fetching movies: {}", e.getMessage());
            throw new MovieApiException("Server error occurred: " + e.getStatusText(), e);
        } catch (RestClientException e) {
            // Handle other RestTemplate-related errors
            logger.error("Error while fetching movies: {}", e.getMessage());
            throw new MovieApiException("An error occurred while fetching movies.", e);
        } catch (Exception e) {
            // Catch any unexpected errors
            logger.error("Unexpected error: {}", e.getMessage());
            throw new MovieApiException("An unexpected error occurred.", e);
        }
    }
}
