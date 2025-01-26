package meetingteam.chatservice.models;

import java.time.LocalDateTime;
import java.util.List;

public class Voting {
    private Boolean isSingleAnswer=false;

    private Boolean isBlocked=false;

    private LocalDateTime endTime;

    private List<VotingOption> options;

    private List<VotingEvent> events;
}
