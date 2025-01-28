package meetingteam.chatservice.services.impls;

import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.dtos.Message.CreateMessageDto;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.models.Event;
import meetingteam.chatservice.models.VotingOption;
import meetingteam.chatservice.models.enums.MessageType;
import meetingteam.chatservice.repositories.MessageRepository;
import meetingteam.chatservice.services.VotingService;
import meetingteam.chatservice.utils.WebsocketUtil;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import meetingteam.commonlibrary.utils.AuthUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VotingServiceImpl implements VotingService {
    private final MessageRepository messageRepo;
    private final WebsocketUtil websocketUtil;

    public void handleMessage(Message message, CreateMessageDto messageDto) {
        if(messageDto.getUsername()==null)
            throw new BadRequestException("Username is required when creating vote message");
        if(message.getVoting()==null)
            throw new BadRequestException("Voting is required when creating vote message");

        message.getVoting().setIsBlocked(false);
        var votingOptions=messageDto.getVoting()
                .getOptions().stream()
                .map(optionStr->new VotingOption(optionStr, new ArrayList()))
                .toList();
        message.getVoting().setOptions(votingOptions);

        var VotingEvent=new Event(messageDto.getUsername()+" has created the vote",LocalDateTime.now());
        message.getVoting().setEvents(List.of(VotingEvent));
    }

    @Override
    public void chooseOption(String messageId, List<String> optionNames, String nickName) {
        var userId= AuthUtil.getUserId();
        var message=messageRepo.findById(messageId).orElseThrow(()->new BadRequestException("Message not found"));

        if(message.getType() != MessageType.VOTING)
            throw new BadRequestException("This is not voting message");

        var voting=message.getVoting();
        var now=LocalDateTime.now();
        if(voting.getIsBlocked()||(voting.getEndTime()!=null&&voting.getEndTime().isBefore(now)))
            throw new BadRequestException("This voting has been blocked");

        if(voting.getIsSingleAnswer()&&optionNames.size()>1)
            throw new BadRequestException("You are allowed to choose just one option in this vote");
        Boolean isVoted=false;
        for(VotingOption option: voting.getOptions()){
            if(optionNames.contains(option.getName())){
                if(!option.getUserIds().contains(userId))
                    option.getUserIds().add(userId);
                else isVoted=true;
            }
            else option.getUserIds().remove(userId);
        }

        var content= nickName+(isVoted?" has changed his options to ":" has choosed ")+
                optionNames.stream().reduce("",(s,name)->s+name+",");
        var event=new Event(content, now);
        if(voting.getEvents()==null) voting.setEvents(new ArrayList());
        voting.getEvents().add(event);

        message.setCreatedAt(now);
        message.setVoting(voting);
        messageRepo.save(message);

        websocketUtil.broadcastMessage(message);
    }

    @Override
    public void blockVoting(String messageId, String nickName) {
        var message=messageRepo.findById(messageId).orElseThrow(()->new BadRequestException("Message not found"));

        if(message.getType() != MessageType.VOTING)
            throw new BadRequestException("This is not voting message");

        var userId= AuthUtil.getUserId();
        if(!message.getSenderId().equals(userId))
            throw new BadRequestException("You are not allowed to block this vote");

        message.getVoting().setIsBlocked(true);
        var event=new Event(nickName+" has blocked the vote ",
                LocalDateTime.now());
        if(message.getVoting().getEvents()==null) message.getVoting().setEvents(new ArrayList<>());
        message.getVoting().getEvents().add(event);

        messageRepo.save(message);
        websocketUtil.broadcastMessage(message);
    }
}
