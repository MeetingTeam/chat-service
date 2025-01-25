package meetingteam.chatservice.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@DynamoDBDocument
public class VotingOption {
    private String name;

    private List<String> userIds;
}
