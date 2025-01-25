package meetingteam.chatservice.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

@Data
@DynamoDBDocument
public class Reaction {
    private String userId;

    private String emojiCode;
}
