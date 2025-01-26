package meetingteam.chatservice.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VotingEvent {
    private String content;

    private LocalDateTime createdAt;
}
