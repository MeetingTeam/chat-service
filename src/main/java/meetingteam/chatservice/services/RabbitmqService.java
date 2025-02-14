package meetingteam.chatservice.services;

import meetingteam.chatservice.models.Message;

public interface RabbitmqService {
    void sendToUser(String userId, String topic, Object payload);
    void sendToTeam(String teamId, String topic, Object payload);
    void broadcastMessage(Message message);
}
