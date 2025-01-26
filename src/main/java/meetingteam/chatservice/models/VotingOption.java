package meetingteam.chatservice.models;

import lombok.Data;

import java.util.List;

@Data
public class VotingOption {
    private String name;

    private List<String> userIds;
}
