package service;

import com.directa24.directa24_back_end_dev_challenge.exception.MovieApiException;
import com.directa24.directa24_back_end_dev_challenge.model.Movie;
import com.directa24.directa24_back_end_dev_challenge.model.MovieResponse;
import com.directa24.directa24_back_end_dev_challenge.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MovieServiceImplTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDirectors_ThresholdMet() {
        MovieResponse responsePage1 = new MovieResponse();
        responsePage1.setData(Arrays.asList(
                new Movie("Movie1", 1, null, null, null, null, "DirectorA", null, null),
                new Movie("Movie2", 1, null, null, null, null, "DirectorB", null, null),
                new Movie("Movie3", 1, null, null, null, null, "DirectorA", null, null)
        ));
        responsePage1.setTotalPages(1);

        when(restTemplate.getForObject(anyString(), eq(MovieResponse.class)))
                .thenReturn(responsePage1);

        List<String> directors = movieService.getDirectors(1);

        assertEquals(List.of("DirectorA"), directors);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(MovieResponse.class));
    }

    @Test
    void testGetDirectors_EmptyResponse() {
        MovieResponse emptyResponse = new MovieResponse();
        emptyResponse.setData(Collections.emptyList());
        emptyResponse.setTotalPages(1);

        when(restTemplate.getForObject(anyString(), eq(MovieResponse.class)))
                .thenReturn(emptyResponse);

        List<String> directors = movieService.getDirectors(0);

        assertTrue(directors.isEmpty());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(MovieResponse.class));
    }

    @Test
    void testGetMovies_Success() {
        MovieResponse response = new MovieResponse();
        response.setData(List.of(new Movie("Movie1", 1, null, null, null, null, "DirectorA", null, null)));
        when(restTemplate.getForObject(anyString(), eq(MovieResponse.class))).thenReturn(response);

        MovieResponse result = movieService.getMovies(1);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals("DirectorA", result.getData().get(0).getDirector());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(MovieResponse.class));
    }

    @Test
    void testGetMovies_ClientError() {
        when(restTemplate.getForObject(anyString(), eq(MovieResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.EXPECTATION_FAILED));

        MovieApiException exception = assertThrows(MovieApiException.class, () -> movieService.getMovies(1));
        assertEquals("Client error occurred: EXPECTATION_FAILED", exception.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(MovieResponse.class));
    }

    @Test
    void testGetMovies_ServerError() {
        when(restTemplate.getForObject(anyString(), eq(MovieResponse.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        MovieApiException exception = assertThrows(MovieApiException.class, () -> movieService.getMovies(1));
        assertEquals("Server error occurred: INTERNAL_SERVER_ERROR", exception.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(MovieResponse.class));
    }

    @Test
    void testGetMovies_RestClientException() {
        when(restTemplate.getForObject(anyString(), eq(MovieResponse.class)))
                .thenThrow(new RestClientException("Connection timeout"));

        MovieApiException exception = assertThrows(MovieApiException.class, () -> movieService.getMovies(1));
        assertEquals("An error occurred while fetching movies.", exception.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(MovieResponse.class));
    }

    @Test
    void testGetMovies_UnexpectedException() {
        when(restTemplate.getForObject(anyString(), eq(MovieResponse.class)))
                .thenThrow(new RuntimeException("Unexpected exception"));

        MovieApiException exception = assertThrows(MovieApiException.class, () -> movieService.getMovies(1));
        assertEquals("An unexpected error occurred.", exception.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(MovieResponse.class));
    }
}
