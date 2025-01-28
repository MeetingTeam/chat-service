package meetingteam.chatservice.repositories;

import meetingteam.chatservice.models.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageRepository extends BaseRepository<Message, String> {
    List<Message> getGroupMessages(String channelId, Pageable pageable);

    List<Message> getPrivateMessages(String userId1, String userId2, Pageable pageable);

    List<Message> getFileMessagesByChannelId(String channelId);

    void deleteByChannelId(String channelId);
}
