package meetingteam.chatservice.dtos.Message;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class CreateReactionDto {
    @NotBlank
    @UUID
    private String messageId;

    private String emojiCode;
}
