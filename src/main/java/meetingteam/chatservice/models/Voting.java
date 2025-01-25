package meetingteam.chatservice.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.time.LocalDateTime;
import java.util.List;

@DynamoDBDocument
public class Voting {
    private Boolean isSingleAnswer=false;

    private Boolean isBlocked=false;

    private LocalDateTime endTime;

    private List<VotingOption> options;

    private List<VotingEvent> events;
}
