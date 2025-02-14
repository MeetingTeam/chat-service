package meetingteam.chatservice.dtos.Message;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTextMessageDto {
    private String teamId;

    private String channelId;

    private String recipientId;

    private String parentMessageId;

    @NotBlank
    private String content;
}
