FROM maven:3.8.6-jdk-11 AS MAVEN_BUILD
COPY ./ ./
RUN mvn clean package -DskipTests
FROM adoptopenjdk:11-jre-hotspot
COPY --from=MAVEN_BUILD ./target/demo-0.0.1-SNAPSHOT.jar /demo.jar
CMD ["java", "-jar", "/demo.jar"]