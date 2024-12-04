# Directa24 Back-End Developer Challenge

This repository contains an implemented solution for the Directa24 back-end development challenge. The goal is to use an external API to obtain a list of directors with the most movies directed, based on a threshold value. Additionally, another endpoint has been added to get a list of movies with all the details provided by the external API, given a page number.

## Requirements

- Java 11 or higher
- Maven
- Internet connection to access the external API

## Description

The application is built using **Spring Boot**. It has a **RESTful endpoint** that allows querying the list of directors whose number of directed movies is greater than a specified threshold. Additionally, another endpoint has been added to obtain the list of movies with all the details provided by the external API, given a page number.

## Endpoints

### 1. Get directors with more movies than a specific threshold:

**GET** `/api/directors?threshold=<thresholdValue>`

This endpoint receives a numeric value (`threshold`) and returns a list of directors whose movie count is strictly greater than the threshold. The response is a list of directors ordered alphabetically.

#### Parameters:
- `threshold` (integer): The threshold value that determines the minimum number of movies a director must have directed to be included in the list.

#### Example request:
```http
GET http://localhost:8080/api/directors?threshold=4
```

#### Example response:
```json
{
  "directors": [
    "Martin Scorsese",
    "Woody Allen"
  ]
}
```

### 2. Get movies from a specific page:

**GET** `/api/movies/search?page=<pageNumber>`

This endpoint returns the list of movies from a specific page of the external API. It is used to obtain full movie details.

#### Parameters:
- `page` (integer): The page number you want to query.

#### Example request:
```http
GET http://localhost:8080/api/movies/search?page=1
```

#### Example response:
```json
{
  "page": 1,
  "per_page": 10,
  "total": 100,
  "total_pages": 10,
  "data": [
    {
      "Title": "The Wolf of Wall Street",
      "Year": 2013,
      "Rated": "R",
      "Released": "25 Dec 2013",
      "Runtime": "180 min",
      "Genre": "Biography, Comedy, Drama",
      "Director": "Martin Scorsese",
      "Writer": "Terence Winter",
      "Actors": "Leonardo DiCaprio, Jonah Hill, Margot Robbie"
    },
    {
      "Title": "Midnight in Paris",
      "Year": 2011,
      "Rated": "PG-13",
      "Released": "20 May 2011",
      "Runtime": "94 min",
      "Genre": "Comedy, Fantasy, Romance",
      "Director": "Woody Allen",
      "Writer": "Woody Allen",
      "Actors": "Owen Wilson, Rachel McAdams, Kathy Bates"
    }
  ]
}
```

## Installation Instructions

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your_username/directa24-backend-challenge.git
    cd directa24-backend-challenge
    ```

2. **Compile the project**:
   If you are using **Maven**:
    ```bash
    mvn clean install
    ```

3. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

4. The application will be available at [http://localhost:8080](http://localhost:8080).

## Implementation Details

- **External API**: The external movie API is queried through the endpoint `https://directa24-movies.wiremockapi.cloud/api/movies/search?page=<pageNumber>`, where `<pageNumber>` is the page number from which the results are fetched.
- Results are paginated to avoid loading too much data at once. For each movie page, the system fetches the list and counts how many times each director appears.
- The method `getDirectors(int threshold)` uses a map to count the number of movies directed by each director and only returns those directors whose movie count is greater than the threshold (`threshold`).

## Dependencies

- **Spring Boot**: For creating the REST server.
- **RestTemplate**: For interacting with the external API.
- **Lombok**: To reduce repetitive code like getters and setters.

## Potential Improvements

- **Error handling**: While specific exceptions (such as client or server errors) are already handled, error messages and exception handling could be improved based on the response from the external API.
- **Performance optimization**: The query for movies by page could be further optimized by using techniques like parallelizing requests for different pages if the number of results is large.
- **Caching**: Implement a caching mechanism to store movie and director responses, so that repeated requests to the external API can be avoided.
- **Testing**: Add more unit and integration tests, especially for cases with unexpected results from the API.

## Contributions

If you would like to contribute to this project, please create a fork and submit a pull request with any improvements or fixes you think are necessary.
