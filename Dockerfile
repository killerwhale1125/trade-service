FROM openjdk:17
COPY build/libs/market-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]