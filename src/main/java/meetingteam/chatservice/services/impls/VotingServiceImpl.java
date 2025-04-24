package meetingteam.chatservice.services.impls;

import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.dtos.Message.CreateTextMessageDto;
import meetingteam.chatservice.dtos.Message.CreateVotingMessageDto;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.models.Event;
import meetingteam.chatservice.models.VotingOption;
import meetingteam.chatservice.models.enums.MessageType;
import meetingteam.chatservice.repositories.MessageRepository;
import meetingteam.chatservice.services.RabbitmqService;
import meetingteam.chatservice.services.TeamService;
import meetingteam.chatservice.services.VotingService;
import meetingteam.chatservice.services.WebsocketService;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import meetingteam.commonlibrary.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VotingServiceImpl implements VotingService {
    private final MessageRepository messageRepo;
    private final WebsocketService websocketService;
    private final TeamService teamService;
    private final ModelMapper modelMapper;

    public void createVoting(CreateVotingMessageDto messageDto){
        var message = modelMapper.map(messageDto, Message.class);

        String userId= AuthUtil.getUserId();
        if(!teamService.isMemberOfTeam(userId, message.getTeamId(), message.getChannelId()))
            throw new AccessDeniedException("You are not member of the team");
        message.setSenderId(userId);
        message.setType(MessageType.VOTING);
        message.setCreatedAt(LocalDateTime.now());

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

        var savedMessage=messageRepo.save(message);
        websocketService.broadcastMessage(savedMessage);
    }

    @Override
    public void chooseOption(String messageId, List<String> optionNames, String nickName) {
        var userId = AuthUtil.getUserId();
        var message = messageRepo.findById(messageId)
                .orElseThrow(() -> new BadRequestException("Message not found"));

        if (message.getType() != MessageType.VOTING) {
            throw new BadRequestException("This is not voting message");
        }

        var voting = message.getVoting();
        var now = LocalDateTime.now();

        boolean votingEnded = voting.getEndTime() != null && voting.getEndTime().isBefore(now);
        if (voting.getIsBlocked() || votingEnded) {
            throw new BadRequestException("This voting has been blocked");
        }

        if (voting.getIsSingleAnswer() && optionNames.size() > 1) {
            throw new BadRequestException("You are allowed to choose just one option in this vote");
        }

        boolean isVoted = false;
        for (VotingOption option : voting.getOptions()) {
            boolean selected = optionNames.contains(option.getName());
            List<String> userIds = option.getUserIds();

            if (selected) {
                if (!userIds.contains(userId)) {
                    userIds.add(userId);
                } else {
                    isVoted = true;
                }
            } else {
                userIds.remove(userId);
            }
        }

        StringBuilder builder = new StringBuilder(nickName);
        builder.append(isVoted ? " has changed his options to " : " has choosed ");
        for (String name : optionNames) {
            builder.append(name).append(",");
        }

        String content = builder.toString();
        var event = new Event(content, now);

        if (voting.getEvents() == null) {
            voting.setEvents(new ArrayList<>());
        }
        voting.getEvents().add(event);
        message.setCreatedAt(now);
        message.setVoting(voting);
        messageRepo.save(message);

        websocketService.broadcastMessage(message);
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
        websocketService.broadcastMessage(message);
    }
}
