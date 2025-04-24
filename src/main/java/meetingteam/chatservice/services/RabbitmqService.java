package meetingteam.chatservice.services;

public interface RabbitmqService {
    void sendToUser(String userId, String topic, Object payload);
    void sendToTeam(String teamId, String topic, Object payload);
}
