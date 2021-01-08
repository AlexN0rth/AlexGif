FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/*.jar
COPY ${JAR_FILE} app.jar
WORKDIR /controller
ENTRYPOINT ["java","-jar","/app.jar"]