package meetingteam.chatservice.services;

import meetingteam.chatservice.models.Message;

import java.util.List;

public interface VotingService {
    void handleMessage(Message message, String username);
    void chooseOption(String messageId, List<String> optionNames);
    void blockVoting(String messageId);
}
