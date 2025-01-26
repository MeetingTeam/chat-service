package meetingteam.chatservice.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Voting {
    private String title;

    private Boolean isSingleAnswer=false;

    private Boolean isBlocked=false;

    private LocalDateTime endTime;

    private List<VotingOption> options;

    private List<VotingEvent> events;
}
