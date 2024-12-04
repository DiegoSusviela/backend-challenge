package com.directa24.directa24_back_end_dev_challenge.service.impl;

import com.directa24.directa24_back_end_dev_challenge.exception.MovieApiException;
import com.directa24.directa24_back_end_dev_challenge.model.Movie;
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

import static java.util.Optional.ofNullable;

@Service
public class MovieServiceImpl implements IMovieService {
    public static final String MOVIE_API_URL = "https://eron-movies.wiremockapi.cloud/api/movies/search?page=";
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);
    private static final int maxPages = 10;
    private final RestTemplate restTemplate;

    public MovieServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Retrieves a list of directors whose movies exceed the specified threshold for the number of appearances. The
     * method makes multiple requests to fetch movie data page by page and counts the occurrences of each director. It
     * processes up to a maximum of 10 pages of data, as defined by the `maxPages` constant.
     * <p>
     * For each page, the method: - Fetches the list of movies - Extracts and counts occurrences of each director
     * <p>
     * Directors whose total movie count exceeds the provided threshold will be included in the result. The list of
     * directors is returned, sorted alphabetically.
     *
     * @param threshold the minimum number of movies a director must have to be included in the result
     * @return a sorted list of directors with movie counts greater than the threshold
     */
    @Override
    public List<String> getDirectors(int threshold) {
        Map<String, Integer> directorMovieCount = new HashMap<>();
        int currentPage = 1;
        int totalPages = maxPages;

        while (currentPage <= totalPages) {
            MovieResponse movieResponse = getMovies(currentPage);
            ofNullable(movieResponse)
                    .map(MovieResponse::getData)
                    .ifPresent(movies -> movies.forEach(
                            movie -> ofNullable(movie)
                                    .map(Movie::getDirector)
                                    .ifPresent(director -> directorMovieCount.merge(director, 1, Integer::sum))
                    ));

            totalPages = ofNullable(movieResponse)
                    .map(MovieResponse::getTotalPages)
                    .orElse(maxPages);
            currentPage++;
        }

        return directorMovieCount.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Endpoint to get a list of movies based on a given page.
     *
     * @param page the page value used for the external movies service.
     * @return a MovieResponse object containing the list of movies.
     */
    public MovieResponse getMovies(int page) {
        String url = MOVIE_API_URL + page;

        try {
            return restTemplate.getForObject(url, MovieResponse.class);
        } catch (HttpClientErrorException e) {
            logger.error("Client error while fetching movies: {}", e.getMessage());
            throw new MovieApiException("Client error occurred: " + e.getStatusText(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Server error while fetching movies: {}", e.getMessage());
            throw new MovieApiException("Server error occurred: " + e.getStatusText(), e);
        } catch (RestClientException e) {
            logger.error("Error while fetching movies: {}", e.getMessage());
            throw new MovieApiException("An error occurred while fetching movies.", e);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            throw new MovieApiException("An unexpected error occurred.", e);
        }
    }
}
