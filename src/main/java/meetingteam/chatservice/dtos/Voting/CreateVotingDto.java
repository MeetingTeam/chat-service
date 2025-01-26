package meetingteam.chatservice.dtos.Voting;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import meetingteam.chatservice.models.VotingEvent;
import meetingteam.chatservice.models.VotingOption;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateVotingDto {
    @NotNull
    private String title;

    @NotNull
    private Boolean isSingleAnswer;

    private LocalDateTime endTime;

    @NotNull
    private List<VotingOption> options;
}
