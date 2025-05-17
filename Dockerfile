FROM eclipse-temurin:17-jdk
COPY build/libs/catalog-service.jar .
ENTRYPOINT ["java", "-jar", "catalog-service.jar"]