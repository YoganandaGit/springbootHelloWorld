#FROM adoptopenjdk/openjdk11:alpine-jre
#COPY target/demo-0.0.1-SNAPSHOT.jar demo.jar
#ENTRYPOINT ["java","-jar","demo.jar"]

FROM eclipse-temurin:21-alpine
EXPOSE 8080
LABEL MAINTAINER="MAINTAINER"

ARG JAR_FILE
RUN echo "JAR_FILE $JAR_FILE"
COPY ${JAR_FILE} application.jar

ENTRYPOINT [ "sh", "-c", "java -jar application.jar" ]

#Use the below command to build the docker image
#docker build --build-arg JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar -t petclinic .
