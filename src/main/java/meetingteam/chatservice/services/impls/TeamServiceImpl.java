package meetingteam.chatservice.services.impls;

import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.configs.ServiceUrlConfig;
import meetingteam.chatservice.services.TeamService;
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
        URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.userServiceUrl())
                .path("/team-member/private/is-member-of-team?userId="+userId+"&channelId="+channelId)
                .build().toUri();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(Boolean.class);
    }
}
