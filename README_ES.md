# Directa24 Back-End Developer Challenge

Este repositorio contiene una solución implementada para el desafío de desarrollo de back-end de Directa24. El objetivo es utilizar una API externa para obtener una lista de los directores de cine con más películas dirigidas, basándose en un valor de umbral. Además, se ha agregado otro endpoint para obtener la lista de películas con todos los detalles proporcionados por la API externa, dado un número de página.

## Requerimientos

- Java 11 o superior
- Maven
- Conexión a internet para acceder a la API externa

## Descripción

La aplicación está construida utilizando **Spring Boot**. Tiene un **endpoint RESTful** que permite consultar la lista de directores cuyo número de películas dirigidas es mayor que un umbral especificado. Además, se agregó otro endpoint para obtener la lista de películas con todos los detalles proporcionados por la API externa, dado un número de página.

## Endpoints

### 1. Obtener los directores con más películas que un umbral específico:

**GET** `/api/directors?threshold=<valorThreshold>`

Este endpoint recibe un valor numérico (`threshold`) y devuelve una lista de directores cuyos contadores de películas dirigidas son estrictamente mayores que el umbral. La respuesta es una lista de directores ordenada alfabéticamente.

#### Parámetros:
- `threshold` (entero): El valor umbral que determina el número mínimo de películas que un director debe haber dirigido para ser incluido en la lista.

#### Ejemplo de petición:
```http
GET http://localhost:8080/api/directors?threshold=4
```

#### Ejemplo de respuesta:
```json
{
  "directors": [
    "Martin Scorsese",
    "Woody Allen"
  ]
}
```

### 2. Obtener las películas de una página específica:

**GET** `/api/movies/search?page=<númeroDePagina>`

Este endpoint devuelve la lista de películas de una página específica de la API externa. Se utiliza para obtener la información completa de las películas.

#### Parámetros:
- `page` (entero): El número de la página que se quiere consultar.

#### Ejemplo de petición:
```http
GET http://localhost:8080/api/movies/search?page=1
```

#### Ejemplo de respuesta:
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

## Instrucciones de instalación

1. **Clonar el repositorio**:
    ```bash
    git clone https://github.com/tu_usuario/directa24-backend-challenge.git
    cd directa24-backend-challenge
    ```

2. **Compilar el proyecto**:
   Si usas **Maven**:
    ```bash
    mvn clean install
    ```

3. **Ejecutar la aplicación**:
    ```bash
    mvn spring-boot:run
    ```

4. La aplicación estará disponible en [http://localhost:8080](http://localhost:8080).

## Detalles de implementación

- **API Externa**: La API externa de películas se consulta a través del endpoint `https://directa24-movies.wiremockapi.cloud/api/movies/search?page=<pageNumber>`, donde `<pageNumber>` es el número de página de la que se obtienen los resultados.
- Se paginan los resultados para evitar cargar demasiados datos a la vez. Por cada página de películas, el sistema obtiene la lista y cuenta cuántas veces se repite cada director.
- El método `getDirectors(int threshold)` usa un mapa para contar el número de películas dirigidas por cada director y devuelve solo aquellos directores cuyo conteo de películas es mayor que el umbral (`threshold`).

## Dependencias

- **Spring Boot**: Para la creación del servidor REST.
- **RestTemplate**: Para la interacción con la API externa.
- **Lombok**: Para la reducción de código repetitivo como getters y setters.

## Mejoras potenciales

- **Manejo de errores**: Aunque ya se maneja excepciones específicas (como errores del cliente o servidor), podrían mejorarse los mensajes de error y la gestión de excepciones en función de la respuesta de la API externa.
- **Optimización de rendimiento**: La consulta de películas por página podría optimizarse aún más, utilizando técnicas como la paralelización de las solicitudes para diferentes páginas si el número de resultados es muy grande.
- **Caché**: Implementar un mecanismo de caché para almacenar las respuestas de las películas y los directores, de modo que se pueda evitar realizar solicitudes a la API externa repetidamente.
- **Pruebas**: Añadir más pruebas unitarias y de integración, especialmente para casos con resultados inesperados de la API.

## Contribuciones

Si deseas contribuir a este proyecto, por favor crea un fork y envía un pull request con las mejoras o correcciones que consideres necesarias.
