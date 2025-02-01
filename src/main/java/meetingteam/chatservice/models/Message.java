package meetingteam.chatservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import meetingteam.chatservice.models.enums.MessageType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Message")
public class Message {
    @Id
    private String id;

    private String senderId;

    private String teamId;

    private String channelId;

    private String recipientId;

    private String parentMessageId;

    private MessageType type;

    private String content;

    private MediaFile mediaFile;

    private List<Reaction> reactions;

    private Voting voting;

    private Reminder reminder;

    private LocalDateTime createdAt;
}

