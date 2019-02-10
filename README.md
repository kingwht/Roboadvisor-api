# RoboAdvisor-api

## Overview 
RoboAdvisor manages portfolios for HSBC's fund balancer.

## Requirements
- Java 8
- Docker containers with dev services 
    - MySQL
    - Adminer

## Build
Build with unit test only:
`mvn clean package`

## Running RoboAdvisor Service
`mvn spring-boot:run`

## Running RoboAdvisor Jar
`java -jar target/<roboadvisor jar>.jar`

## Checking Swagger
Once the service/jar is running, the swagger page can be reached at `localhost:5000/swagger-ui.html`
