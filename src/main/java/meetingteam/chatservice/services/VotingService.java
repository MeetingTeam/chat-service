package meetingteam.chatservice.services;

import meetingteam.chatservice.dtos.Message.CreateVotingMessageDto;

import java.util.List;

public interface VotingService {
    void createVoting(CreateVotingMessageDto messageDto);
    void chooseOption(String messageId, List<String> optionNames, String nickName);
    void blockVoting(String messageId, String nickName);
}
