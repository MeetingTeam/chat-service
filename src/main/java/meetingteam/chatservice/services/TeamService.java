package meetingteam.chatservice.services;

public interface TeamService {
    boolean isMemberOfTeam(String userId, String teamId, String channelId);
    boolean requestToJoinTeam(String teamId);
}
