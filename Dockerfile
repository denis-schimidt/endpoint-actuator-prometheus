FROM maven:3.6.0-jdk-8-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:8-jre-slim
ENV ENDPOINT_ACTUATOR /opt/endpoint-actuator
COPY --from=build /home/app/target/*.jar ${ENDPOINT_ACTUATOR}/endpoint-actuator.jar
WORKDIR ${ENDPOINT_ACTUATOR}
COPY src/main/resources resources
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/endpoint-actuator/endpoint-actuator.jar"]