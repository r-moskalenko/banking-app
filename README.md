## Documentation

### Tech stack

During development used following technologies

1. Java as programming language.
2. Spring Boot as programming framework for Web App
3. Postgres as relational database for storing data
4. Swagger 3 as self-documented REST API web interface
5. Mockito for mocking Web MVC

## How to run app locally

The following programs must be installed on your computer:
* Docker CLI
* Java 17

To run app locally you need to run following commands in the terminal:

1. `docker compose -f docker/docker-compose.yml up -d`
2. `./gradlew bootRun`
3. visit swagger page http://localhost:8080/swagger-ui/index.html
4. to run automated tests use `./gradlew clean test --info`



