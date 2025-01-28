package meetingteam.chatservice.services.impls;

import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.configs.ServiceUrlConfig;
import meetingteam.chatservice.services.TeamService;
import meetingteam.commonlibrary.utils.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final ServiceUrlConfig serviceUrlConfig;
    private final RestClient restClient;

    @Override
    public boolean isMemberOfTeam(String userId, String channelId) {
        URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.teamServiceUrl())
                .path("/team-member/private/is-member-of-team")
                .queryParam("userId", userId)
                .queryParam("channelId", channelId)
                .build().toUri();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(Boolean.class);
    }

    public boolean requestToJoinTeam(String teamId){
        String jwtToken= AuthUtil.getJwtToken();
        URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.teamServiceUrl())
                .path("/team/private/joined/"+teamId)
                .build().toUri();

        return restClient.post()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwtToken))
                .retrieve()
                .body(Boolean.class);
    }
}
