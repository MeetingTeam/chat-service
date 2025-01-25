package meetingteam.chatservice.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import meetingteam.chatservice.models.enums.MessageType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDBTable(tableName = "Message")
public class Message {
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    private String senderId;

    //teamId cho th JOINREQUEST
    private String channelId;

    private String recipientId;

    private String parentMessageId;

    @DynamoDBTypeConvertedEnum
    private MessageType type;

    private String content;

    private MediaFile mediaFile;

    private List<Reaction> reactions;

    private Voting voting;

    private Reminder reminder;

    @DynamoDBRangeKey
    private LocalDateTime createdAt;
}

