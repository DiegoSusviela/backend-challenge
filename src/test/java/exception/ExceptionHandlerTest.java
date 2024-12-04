package exception;

import com.directa24.directa24_back_end_dev_challenge.exception.ExceptionHandler;
import com.directa24.directa24_back_end_dev_challenge.exception.MovieApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExceptionHandlerTest {
    private ExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ExceptionHandler();
    }

    @Test
    void testHandleMovieApiException() {
        MovieApiException exception = new MovieApiException("Movie API error");

        ResponseEntity<String> response = exceptionHandler.handleMovieApiException(exception);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Movie API error", response.getBody());
    }

    @Test
    void testHandleMethodArgumentTypeMismatch() {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("threshold");
        when(exception.getMessage()).thenReturn("Failed to convert value");

        ResponseEntity<String> response = exceptionHandler.handleMethodArgumentTypeMismatch(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid value for parameter 'threshold'. Please provide a valid value.", response.getBody());
    }

    @Test
    void testHandleMissingParameter() {
        MissingServletRequestParameterException exception =
                new MissingServletRequestParameterException("threshold", "int");

        ResponseEntity<String> response = exceptionHandler.handleMissingParameter(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The parameter 'threshold' is required and is missing.", response.getBody());
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Something went wrong");

        ResponseEntity<String> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred.", response.getBody());
    }
}
