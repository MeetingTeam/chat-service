package meetingteam.chatservice.services.impls;

import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.dtos.Message.CreateTextMessageDto;
import meetingteam.chatservice.models.MediaFile;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.models.Reaction;
import meetingteam.chatservice.models.enums.MessageType;
import meetingteam.chatservice.repositories.MessageRepository;
import meetingteam.chatservice.services.*;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import meetingteam.commonlibrary.utils.AuthUtil;
import meetingteam.commonlibrary.utils.PageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepo;
    private final MediaFileService mediaFileService;
    private final UserService userService;
    private final TeamService teamService;
    private final WebsocketService websocketService;
    private final ModelMapper modelMapper;

    @Override
    public void receiveTextMessage(CreateTextMessageDto messageDto) {
        var message = modelMapper.map(messageDto, Message.class);
        String userId= AuthUtil.getUserId();

        if(message.getRecipientId()!=null){
            if(!userService.isFriend(userId, message.getRecipientId()))
                throw new AccessDeniedException("You are not friend of the recipient");
            message.setChannelId(null);
        }
        else if(message.getChannelId()!=null){
            if(!teamService.isMemberOfTeam(userId, message.getTeamId(), message.getChannelId()))
                throw new AccessDeniedException("You are not member of the team");
        }
        else throw new BadRequestException("Either RecipientId or ChannelId must not be null");

        message.setSenderId(userId);
        message.setCreatedAt(LocalDateTime.now());
        message.setType(MessageType.TEXT);

        var savedMessage=messageRepo.save(message);
        websocketService.broadcastMessage(savedMessage);
    }

    @Override
    public Message unsendMessage(String messageId) {
        Message message= messageRepo.findById(messageId).orElseThrow(()->new BadRequestException("Message not found"));
        if(!message.getSenderId().equals(AuthUtil.getUserId()))
            throw new AccessDeniedException("You are not authorized to modify this message");

        switch (message.getType()) {
            case TEXT:
                message.setContent(null);
                break;
            case VOTING:
                message.setVoting(null);
                break;
            case REMINDER:
                message.setReminder(null);
                break;
            case AUDIO, VIDEO, IMAGE, DOCUMENT:
                mediaFileService.deleteMediaFile(message.getMediaFile());
                message.setMediaFile(null);
                break;
            default:
                throw new BadRequestException("Unsupported message type");
        }
        message.setType(MessageType.UNSEND);

        var savedMessage=messageRepo.save(message);
        websocketService.broadcastMessage(savedMessage);
        return savedMessage;
    }

    @Override
    public Message reactMessage(String messageId, String emojiCode) {
        Message message= messageRepo.findById(messageId).orElseThrow(()->new BadRequestException("Message not found"));
        String userId=AuthUtil.getUserId();
        Reaction reaction= new Reaction(userId, emojiCode);

        var reactions=message.getReactions();
        if(reactions==null) reactions=new ArrayList();
        int i=0;
        for(;i<reactions.size(); i++) {
            if(reactions.get(i).getUserId().equals(userId)) {
                if(emojiCode==null) reactions.remove(i);
                else reactions.set(i, reaction);
                break;
            }
        }
        if(i==reactions.size() && emojiCode!=null)
            reactions.add(reaction);
        message.setReactions(reactions);

        messageRepo.save(message);
        websocketService.broadcastMessage(message);
        return message;
    }

    @Override
    public List<Message> getTextChannelMessages(Integer receivedMessageNum, String channelId) {
        String userId=AuthUtil.getUserId();
        if(!teamService.isMemberOfTeam(userId, null, channelId))
            throw new AccessDeniedException("You do not have permission to read messages from the given channel");

        int pageSize= PageUtil.findBestPageSize(receivedMessageNum);
        PageRequest pageRequest=PageRequest.of(receivedMessageNum/pageSize,pageSize);

        var result= messageRepo.getGroupMessages(channelId, pageRequest);
        Collections.reverse(result);
        return result;
    }

    @Override
    public List<Message> getPrivateMessages(Integer receivedMessageNum, String friendId) {
        String userId=AuthUtil.getUserId();
        if(!userService.isFriend(userId,friendId))
            throw new AccessDeniedException("You do not have permission to read messages from the given person");

        int pageSize= PageUtil.findBestPageSize(receivedMessageNum);
        PageRequest pageRequest=PageRequest.of(receivedMessageNum/pageSize,pageSize);

        var result= messageRepo.getPrivateMessages(userId, friendId, pageRequest);
        Collections.reverse(result);
        return result;
    }

    @Override
    public void deleteMessagesByChannelId(String channelId) {
        List<MediaFile> mediaFiles= messageRepo.getFileMessagesByChannelId(channelId).stream()
                        .map(message->message.getMediaFile())
                        .toList();
        if(mediaFiles!=null&&!mediaFiles.isEmpty())
            mediaFileService.deleteMediaFiles(mediaFiles);
        messageRepo.deleteByChannelId(channelId);
    }
}
