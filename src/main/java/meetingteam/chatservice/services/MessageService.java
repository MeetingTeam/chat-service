package meetingteam.chatservice.services;

import meetingteam.chatservice.dtos.Message.CreateTextMessageDto;
import meetingteam.chatservice.models.Message;

import java.util.List;

public interface MessageService {
    void receiveTextMessage(CreateTextMessageDto messageDto);
    Message unsendMessage(String messageId);
    Message reactMessage(String messageId, String emojiCode);
    List<Message> getTextChannelMessages(Integer receivedMessageNum, String channelId);
    List<Message> getPrivateMessages(Integer receivedMessageNum, String friendId);
    void deleteMessagesByChannelId(String channelId);
    void deleteMessagesByTeamId(String teamId);
}
