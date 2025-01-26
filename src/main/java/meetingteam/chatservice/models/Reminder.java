package meetingteam.chatservice.models;

import lombok.Data;
import meetingteam.chatservice.models.enums.ReminderInterval;

import java.time.LocalDateTime;

@Data
public class Reminder {
    private LocalDateTime reminderTime;

    private ReminderInterval interval;
}
