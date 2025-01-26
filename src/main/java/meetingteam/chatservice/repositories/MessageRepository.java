package meetingteam.chatservice.repositories;

import jakarta.transaction.Transactional;
import meetingteam.chatservice.models.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    @Query("select m from Message m where m.channelId=?1 order by m.createdAt DESC")
    List<Message> getGroupMessages(String channelId, Pageable pageable);

    @Query("select m from Message m where (m.senderId=?1 and m.recipientId=?2) or (m.senderId=?2 and m.recipientId=?1) order by m.createdAt DESC")
    List<Message> getPrivateMessages(String userId1, String userId2, Pageable pageable);

    @Query("select m from Message m where m.channelId=?1 and m.type in " +
            "(meetingteam.chatservice.models.enums.MessageType.AUDIO," +
            "meetingteam.chatservice.models.enums.MessageType.IMAGE," +
            "meetingteam.chatservice.models.enums.MessageType.VIDEO," +
            "meetingteam.chatservice.models.enums.MessageType.DOCUMENT)")
    List<Message> getFileMessagesByChannelId(String channelId);

    @Modifying
    @Transactional
    void deleteByChannelId(String channelId);
}
