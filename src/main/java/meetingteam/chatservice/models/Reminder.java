package meetingteam.chatservice.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;
import meetingteam.chatservice.models.enums.ReminderInterval;

import java.time.LocalDateTime;

@Data
@DynamoDBDocument
public class Reminder {
    private LocalDateTime reminderTime;

    private ReminderInterval interval;
}
