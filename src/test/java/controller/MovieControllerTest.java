package controller;

import com.directa24.directa24_back_end_dev_challenge.Directa24BackEndDevChallengeApplication;
import com.directa24.directa24_back_end_dev_challenge.controller.MovieController;
import com.directa24.directa24_back_end_dev_challenge.model.DirectorResponse;
import com.directa24.directa24_back_end_dev_challenge.model.Movie;
import com.directa24.directa24_back_end_dev_challenge.model.MovieResponse;
import com.directa24.directa24_back_end_dev_challenge.service.impl.MovieServiceImpl;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Directa24BackEndDevChallengeApplication.class)
class MovieControllerTest {
    @MockBean
    private MovieServiceImpl movieService;

    @InjectMocks
    @Autowired
    private MovieController movieController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDirectors_ReturnsDirectors() {
        List<String> directors = Arrays.asList("DirectorA", "DirectorB");
        when(movieService.getDirectors(1)).thenReturn(directors);

        ResponseEntity<?> response = movieController.getDirectors(1);

        assertNotNull(response);
        assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof DirectorResponse);

        DirectorResponse directorResponse = (DirectorResponse) response.getBody();
        assertNotNull(directorResponse);
        assertEquals(directors, directorResponse.getDirectors());

        verify(movieService, times(1)).getDirectors(1);
    }

    @Test
    void testGetDirectors_NoDirectors() {
        when(movieService.getDirectors(5)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = movieController.getDirectors(5);

        assertNotNull(response);
        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof String);

        String message = (String) response.getBody();
        assertEquals("No directors found with more than 5 movies.", message);

        verify(movieService, times(1)).getDirectors(5);
    }

    @Test
    void testGetDirectors_ThresholdNotMet() {
        when(movieService.getDirectors(10)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = movieController.getDirectors(10);

        assertNotNull(response);
        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof String);

        String message = (String) response.getBody();
        assertEquals("No directors found with more than 10 movies.", message);

        verify(movieService, times(1)).getDirectors(10);
    }

    @Test
    void testGetMovies_ReturnsMovies() {
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setData(Arrays.asList(
                new Movie("Movie1", 1, null, null, null, null, "DirectorA", null, null),
                new Movie("Movie2", 1, null, null, null, null, "DirectorB", null, null)
        ));
        when(movieService.getMovies(1)).thenReturn(movieResponse);

        MovieResponse response = movieController.getMovies(1);

        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertEquals("DirectorA", response.getData().get(0).getDirector());

        verify(movieService, times(1)).getMovies(1);
    }

    @Test
    void testGetMovies_EmptyResponse() {
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setData(Collections.emptyList());
        when(movieService.getMovies(2)).thenReturn(movieResponse);

        MovieResponse response = movieController.getMovies(2);

        assertNotNull(response);
        assertTrue(response.getData().isEmpty());

        verify(movieService, times(1)).getMovies(2);
    }
}
