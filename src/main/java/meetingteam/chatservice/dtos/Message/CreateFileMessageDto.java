package meetingteam.chatservice.dtos.Message;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import meetingteam.chatservice.dtos.MediaFile.CreateMediaFile;

@Data
public class CreateFileMessageDto {
    private String teamId;

    private String channelId;

    private String recipientId;

    private String parentMessageId;

    @Valid
    @NotNull
    private CreateMediaFile mediaFile;
}
