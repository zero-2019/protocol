FROM openjdk:8-jdk-alpine
ADD protocol-module.jar app.jar

VOLUME ["/opt/visdata/"]
ENV EUREKA_HOST=eureka-cn


ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8996
