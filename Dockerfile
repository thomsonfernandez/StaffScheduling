FROM openjdk:18-jdk-alpine
MAINTAINER thomsonfernandez
ADD target/staff-scheduling-0.0.1-SNAPSHOT.jar staff-scheduling-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "staff-scheduling-0.0.1-SNAPSHOT.jar"]