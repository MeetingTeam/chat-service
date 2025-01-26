package meetingteam.chatservice.dtos.Message;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import meetingteam.chatservice.dtos.MediaFile.CreateMediaFile;
import meetingteam.chatservice.dtos.Reminder.CreateReminderDto;
import meetingteam.chatservice.dtos.Voting.CreateVotingDto;
import meetingteam.chatservice.models.Reminder;
import meetingteam.chatservice.models.Voting;
import meetingteam.chatservice.models.converters.ReminderConverter;
import meetingteam.chatservice.models.enums.MessageType;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;

@Data
public class CreateMessageDto {
    @UUID
    private String channelId;

    @UUID
    private String teamId;

    private String recipientId;

    @UUID
    private String parentMessageId;

    private MessageType type;

    private String content;

    @Valid
    private CreateMediaFile mediaFile;

    @Valid
    private CreateVotingDto voting;

    @Valid
    private CreateReminderDto reminder;

    @NotNull
    private LocalDateTime createdAt;

    private String username;
}
