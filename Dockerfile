FROM openjdk:8-jre-alpine

WORKDIR /library

ENV OPENAPI_BASE_PATH=/v1

COPY target/openapi-library-1.0.0.jar /library/openapi-library.jar

EXPOSE 8080

CMD ["java", "-Dopenapi.openAPILibrary.base-path=${OPENAPI_BASE_PATH}", "-jar", "/library/openapi-library.jar"]
