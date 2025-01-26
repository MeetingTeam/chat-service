package meetingteam.chatservice.repositories;

import meetingteam.chatservice.models.RequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestMessageRepository extends JpaRepository<RequestMessage, String> {
    Boolean existsBySenderIdAndTeamId(String senderId,String teamId);

    @Query("select rm from RequestMessage rm where rm.recipientId=?1 or (rm.senderId=?1 and rm.recipientId is not null)")
    List<RequestMessage> getFriendRequests(String userId);

    @Query("select rm from RequestMessage rm where rm.teamId=?1")
    List<RequestMessage> getTeamRequests(String teamId);

    @Query("select rm from RequestMessage rm where rm.senderId=?1 and rm.teamId is not null")
    List<RequestMessage> getSendedRequests(String senderId);
}
