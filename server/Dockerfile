# Build stage
FROM maven:3.9.9 AS build
COPY src /ac-server/src
COPY pom.xml /ac-server
COPY externalModules /ac-server/externalModules

ARG MAVEN_PROFILES
RUN if [ -z ${MAVEN_PROFILES} ]; then mvn -f /ac-server/pom.xml clean package -DskipTests; else mvn -f /ac-server/pom.xml clean package -P${MAVEN_PROFILES} -DskipTests; fi

# Package stage
FROM openjdk:21-jdk-bullseye
COPY --from=build /ac-server/target/*.jar ac-server.jar

ARG BE_GRPC_DOCKER_PATH
COPY --from=build ${BE_GRPC_DOCKER_PATH} ${BE_GRPC_DOCKER_PATH}
COPY --from=build /ac-server/externalModules /externalModules

EXPOSE 8080

COPY ./wait-for-it.sh .
RUN chmod +x ./wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh", "db:5432", "--", "java","-jar","ac-server.jar"]