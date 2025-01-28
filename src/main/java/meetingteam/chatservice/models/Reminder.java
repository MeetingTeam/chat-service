package meetingteam.chatservice.models;

import lombok.Data;
import meetingteam.chatservice.models.enums.ReminderInterval;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Reminder {
    private String title;

    private Boolean isClosed;

    private List<Event> events;

    private LocalDateTime reminderTime;

    private ReminderInterval interval;
}
