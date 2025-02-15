FROM openjdk:17-jdk-alpine

## Change directory
WORKDIR /app

## Create non-root user
RUN adduser -D chat_service
RUN chown -R chat_service. /app
USER chat_service

## Copy war file and run app
COPY target/chat-service-0.0.1-SNAPSHOT.war chat_service.war
ENTRYPOINT ["java","-jar","chat_service.war"]

## Expose port 8082
EXPOSE 8082