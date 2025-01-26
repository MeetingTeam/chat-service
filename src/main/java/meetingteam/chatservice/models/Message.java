package meetingteam.chatservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import meetingteam.chatservice.models.converters.MediaFileConverter;
import meetingteam.chatservice.models.converters.ReactionsConverter;
import meetingteam.chatservice.models.converters.ReminderConverter;
import meetingteam.chatservice.models.converters.VotingConverter;
import meetingteam.chatservice.models.enums.MessageType;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Message {
    @Id @UuidGenerator
    private String id;

    @Column(nullable = false)
    private String senderId;

    private String channelId;

    private String teamId;

    private String recipientId;

    private String parentMessageId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    @Convert(converter= MediaFileConverter.class)
    private MediaFile mediaFile;

    @Column(columnDefinition = "TEXT")
    @Convert(converter= ReactionsConverter.class)
    private List<Reaction> reactions;

    @Column(columnDefinition = "TEXT")
    @Convert(converter= VotingConverter.class)
    private Voting voting;

    @Column(columnDefinition = "TEXT")
    @Convert(converter= ReminderConverter.class)
    private Reminder reminder;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

