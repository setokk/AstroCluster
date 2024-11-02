# Build stage
FROM maven:latest AS build
COPY src /home/app/src
COPY pom.xml /home/app

ARG MAVEN_PROFILES
RUN if [ -z $MAVEN_PROFILES ]; then mvn -f /home/app/pom.xml clean package -DskipTests; else mvn -f /home/app/pom.xml clean package -P${MAVEN_PROFILES} -DskipTests; fi

# Package stage
FROM openjdk:21-jdk-bullseye
COPY --from=build /home/app/target/*.jar ac-server.jar

EXPOSE 8080

COPY ./wait-for-it.sh .
RUN chmod +x ./wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh", "db:5432", "--", "java","-jar","ac-server.jar"]