package meetingteam.chatservice.dtos.Reminder;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import meetingteam.chatservice.models.enums.ReminderInterval;

import java.time.LocalDateTime;

@Data
public class CreateReminderDto {
    @NotNull
    private String title;

    @NotNull
    private LocalDateTime reminderTime;

    @NotNull
    private ReminderInterval interval;
}
