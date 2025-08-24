FROM openjdk:17-jdk-alpine

# Change directory
WORKDIR /app

# Download the OTel Java Agent
ARG OTEL_AGENT_VERSION=2.8.0
ARG OTEL_AGENT_JAR=/app/opentelemetry-javaagent.jar
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar $OTEL_AGENT_JAR

# Copy war file
COPY target/chat-service-0.0.1-SNAPSHOT.war chat_service.war

# Create non-root user
RUN adduser -D chat_service
RUN chown -R chat_service:chat_service /app
USER chat_service

# Run app
ENTRYPOINT ["sh","-c","java -javaagent:opentelemetry-javaagent.jar -jar -Dspring.config.location=${CONFIG_PATH} chat_service.war"]

# Expose port 8082
EXPOSE 8082