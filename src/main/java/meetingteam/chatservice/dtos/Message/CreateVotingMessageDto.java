package meetingteam.chatservice.dtos.Message;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import meetingteam.chatservice.dtos.Voting.CreateVotingDto;

@Data
public class CreateVotingMessageDto {
    @NotBlank
    private String teamId;

    @NotBlank
    private String channelId;

    @Valid
    @NotNull
    private CreateVotingDto voting;

    @NotBlank
    private String username;
}
