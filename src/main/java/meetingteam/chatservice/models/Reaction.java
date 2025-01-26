package meetingteam.chatservice.models;

import lombok.Data;

@Data
public class Reaction {
    private String userId;

    private String emojiCode;
}
