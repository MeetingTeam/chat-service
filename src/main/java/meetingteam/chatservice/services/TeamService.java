package meetingteam.chatservice.services;

public interface TeamService {
    boolean isMemberOfTeam(String userId, String channelId);
}
