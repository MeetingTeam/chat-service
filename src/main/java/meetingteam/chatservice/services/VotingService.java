package meetingteam.chatservice.services;

import meetingteam.chatservice.dtos.Message.CreateMessageDto;
import meetingteam.chatservice.models.Message;

import java.util.List;

public interface VotingService {
    void handleMessage(Message message, CreateMessageDto messageDto);
    void chooseOption(String messageId, List<String> optionNames, String nickName);
    void blockVoting(String messageId, String nickName);
}
