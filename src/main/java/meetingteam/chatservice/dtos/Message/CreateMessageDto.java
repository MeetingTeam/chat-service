package meetingteam.chatservice.dtos.Message;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import meetingteam.chatservice.dtos.MediaFile.CreateMediaFile;
import meetingteam.chatservice.dtos.Reminder.CreateReminderDto;
import meetingteam.chatservice.dtos.Voting.CreateVotingDto;
import meetingteam.chatservice.models.enums.MessageType;

import java.time.LocalDateTime;

@Data
public class CreateMessageDto {
    private String teamId;

    private String channelId;

    private String recipientId;

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
