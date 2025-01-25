package meetingteam.chatservice.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@DynamoDBDocument
public class VotingEvent {
    private String content;

    private LocalDateTime createdAt;
}
