FROM openjdk:21
ARG WAR_FILE=target/*.war
COPY target/demo-0.0.1-SNAPSHOT.war demo.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/demo.war"]