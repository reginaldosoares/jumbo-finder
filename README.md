# Jumbo Finder Application


## overview

apps tech-stack:

- frontend
  - [vuejs](https://github.com/vuejs/vue) as interface/frontend js framework 
  - [axios](https://github.com/axios/axios) as async/promise js http client 
  - [vue2 gmaps library](https://www.npmjs.com/package/vue2-google-maps) gmaps library component for vue

- backend
  - [kotlin](https://github.com/JetBrains/kotlin) as programming language
  - [spring webflux/reactor](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) as reactive web framework
  - [spring core](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#spring-core) as dependency injection framework
  - [jackson](https://github.com/FasterXML/jackson-module-kotlin) as data bind serialization/deserialization
  - [junit](https://github.com/junit-team/junit5) testing tool
  
- infrastructure
  - [docker-compose](https://github.com/docker/compose) for defining and running multi-containers
  - [mongodb](https://github.com/mongodb/mongo) document database / geospatial query capable
  - [gradle](https://github.com/gradle/gradle) backend build tool  
  - [npm](https://github.com/gradle/gradle) backend build tool    
  - [swagger](https://github.com/swagger-api/swagger-core) openapi specification generator 
  - [nginx](https://github.com/nginx/nginx) http server / static content    




## running services using docker 
> docker-compose up 

frontend is configured to start on [4000](http://localhost:4000/)

backend is configured to start on [8080](http://localhost:8080/api/) with `api` context

openapi/swagger is served on [8080](http://localhost:8080/) on index page.


## individual deployment

# The following commands need to be ran inside the folders specified at the subtitles in the
# "dir" propertie values

### database (dir: root)
> docker-compose -f docker/docker-compose-mongo-only.yml up

### backend (dir: stores-finder-backend)  
**build:**
> ./gradlew clean build

**start backend server:**
> ./gradlew bootRun


### frontend  (dir: stores-finder-frontend) 

**project setup:**
> npm install

**build/compiles:**
> npm build

**start the server:**
> npm serve --port 4000
