# The (Book) library application

This is an implementation of an OpenAPI generated (book) library based on Spring-Boot.

## First Steps (on linux based systems)
```bash
sudo mysql
```
```mysql
CREATE USER 'library'@'localhost'  IDENTIFIED BY 'library';
create database books;
GRANT ALL PRIVILEGES ON books.* TO 'library'@'localhost';
```
then login as library
```bash
mysql -u library -plibrary
USE books;
CREATE TABLE books (
   id              BIGINT                   NOT NULL AUTO_INCREMENT,
   title           VARCHAR(100)             NOT NULL,
   author          VARCHAR(50)              NOT NULL,
   published_date  DATE                     NULL,
   isbn            VARCHAR(20)              NULL,
   status          VARCHAR(20)              NULL,
   price           DECIMAL(9, 2)            NULL,
   CONSTRAINT pk_books         PRIMARY KEY (id),
   CONSTRAINT uk_books_isbn    UNIQUE KEY  (isbn)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

```

## Overview
- generated sources including interfaces are placed in the `target/`
Start your server as a simple Spring-Boot application
```
mvn spring-boot:run
```
Or package it then run it as a Java application
```
mvn package
java -jar target/openapi-library-{VERSION}.jar
```

You can view the api documentation in swagger-ui by pointing to  
http://localhost:8080/

## Database

The database will be pre-populated by the BookApiDelegateImpl
```mysql
use books;
select * from books;
```
```
+----+-------------+-------------+----------------+---------------+-----------+-------+
| id | title       | author      | published_date | isbn          | status    | price |
+----+-------------+-------------+----------------+---------------+-----------+-------+
|  1 | Novel 1     | author 1    | 2025-05-26     | 9786155131813 | available |  0.00 |
|  2 | Novel 2     | author 2    | 2025-05-26     | 9783050034157 | available |  0.00 |
|  3 | Novel 3     | author 3    | 2025-05-26     | 9786069722947 | requested |  0.00 |
|  4 | Reference 1 | author 4    | 2025-05-26     | 9784139697805 | available |  0.00 |
|  5 | Reference 2 | author 5    | 2025-05-26     | 9781498504256 | out       |  0.00 |
|  6 | Reference 3 | author 6    | 2025-05-26     | 9783133977739 | out       |  0.00 |
|  7 | Magazine 1  | publisher 1 | 2025-05-26     | 9786948825578 | available |  0.00 |
|  8 | Magazine 2  | publisher 2 | 2025-05-26     | 9783417329506 | available |  0.00 |
|  9 | Magazine 3  | publisher 3 | 2025-05-26     | 9788432082993 | available |  0.00 |
| 10 | Romance 1   | author 7    | 2025-05-26     | 9781192584752 | out       |  0.00 |
+----+-------------+-------------+----------------+---------------+-----------+-------+
10 rows in set (0.00 sec)

```
## running curl(s)
To retrieve a book
```bash
curl -X GET "http://localhost:8080/book/1" -H  "accept: application/xml"
```
To add one
```bash
curl -X POST "http://localhost:8080/book" -H  "accept: */*" -H  "Content-Type: application/json" -d "{\"name\":\"book\",\"id\":10002,\"title\":\"my first\",\"isbn\":\"9788432082993\",\"author\":\"JRR\",\"status\":\"available\"}"
```
## ISBN

Random ISBN numbers are generated using this function BookApiDelegateImpl.createIsbn
ISBN's are validated in the BookApiDelegateImpl class when a Book is added

## Docker

To start the server via docker, please run the following commands:
```sh
docker pull openapitools/openapi-library
docker run -d -e OPENAPI_BASE_PATH=/v1 -p 80:8080 openapitools/openapi-library
```

Ref: https://hub.docker.com/r/openapitools/openapi-library/

## Security

### API key
Use `special-key` for endpoints protected by the API key

### OAuth2
By default the server supports the implicit and the password flow (even though only the implicit flow is described in the OAI spec)
The default credentials are:
* client-id: sample-client-id
* client-secret: secret
* username: user
* password: user

## Configuration

Spring parameters in application.properties:
* Server port : `server.port` (default=8080)
* API base path : `openapi.openAPILibrary.base-path` (default=/v1). In the docker image the base path can also be set with the `OPENAPI_BASE_PATH` environment variable.

Environment variables:
* `DISABLE_API_KEY` : if set to "1", the server will not check the api key for the relevant endpoints.
* `DISABLE_OAUTH` : if set to "1", the server will not check for an OAuth2 access token.

## License

[Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0)