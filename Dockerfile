FROM openjdk:17-jdk-alpine

# Change directory
WORKDIR /app

# Copy war file
COPY target/chat-service-0.0.1-SNAPSHOT.war chat_service.war

# Create non-root user
RUN adduser -D chat_service
RUN chown -R chat_service:chat_service /app
USER chat_service

# Run app
ENTRYPOINT ["sh","-c","java -jar -Dspring.config.location=$CONFIG_PATH chat_service.war"]

# Expose port 8082
EXPOSE 8082