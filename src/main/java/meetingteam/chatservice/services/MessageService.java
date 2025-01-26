package meetingteam.chatservice.services;

import meetingteam.chatservice.dtos.Message.CreateMessageDto;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.models.Reaction;

import java.util.List;

public interface MessageService {
    Message receiveMessage(CreateMessageDto messageDto);
    Message unsendMessage(String messageId);
    Message reactMessage(String messageId, String emojiCode);
    List<Message> getTextChannelMessages(Integer receivedMessageNum, String channelId);
    List<Message> getPrivateMessages(Integer receivedMessageNum, String friendId);
    void deleteMessagesByChannelId(String channelId);
}
