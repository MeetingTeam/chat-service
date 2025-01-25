package meetingteam.chatservice.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsServicesConfig {
    @Value("${aws.access-key}")
    private String awsAccessKey;

    @Value("${aws.access-secret}")
    private String awsAccessSecret;

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public StaticCredentialsProvider credentialsProvider() {
        AwsBasicCredentials awsCreds= AwsBasicCredentials.create(awsAccessKey, awsAccessSecret);
        return StaticCredentialsProvider.create(awsCreds);
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(credentialsProvider())
                .region(Region.of(awsRegion))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .credentialsProvider(credentialsProvider())
                .region(Region.of(awsRegion))
                .build();
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(credentialsProvider())
                .build();
    }
}
