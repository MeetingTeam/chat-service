package meetingteam.chatservice.services.impls;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.configs.ServiceUrlConfig;
import meetingteam.chatservice.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ServiceUrlConfig serviceUrlConfig;
    private final RestClient restClient;

    @Override
    @Retry(name="restApi")
    @CircuitBreaker(name="restCircuitBreaker")
    public boolean isFriend(String userId, String friendId) {
        URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.userServiceUrl())
                .path("/friend/private/is-friend")
                .queryParam("userId", userId)
                .queryParam("friendId", friendId)
                .build().toUri();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(Boolean.class);
    }
}
