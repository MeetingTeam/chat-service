package meetingteam.chatservice.services.impls;

import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.models.VotingEvent;
import meetingteam.chatservice.services.VotingService;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VotingServiceImpl implements VotingService {
    public void handleMessage(Message message, String username) {
        if(username==null)
            throw new BadRequestException("Username is required when creating vote message");

        message.getVoting().setIsBlocked(false);

        var VotingEvent=new VotingEvent(username+" has created the vote",LocalDateTime.now());
        message.getVoting().setEvents(List.of(VotingEvent));
    }
}
