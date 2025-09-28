package meetingteam.chatservice.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.configs.ServiceUrlConfig;
import meetingteam.commonlibrary.utils.AuthUtil;

@RestController
@RequestMapping("/anomaly")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class AnomalyController {
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;

    @GetMapping("/get-friends")
    public ResponseEntity<Void> getFriends(
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize
    ){
        String jwtToken = AuthUtil.getJwtToken();

        var uriBuilder= UriComponentsBuilder
            .fromHttpUrl(serviceUrlConfig.userServiceUrl())
            .path("/friends")
            .queryParam("pageNo", pageNo)
            .queryParam("pageSize", pageSize);
        URI uri = uriBuilder.build().toUri();

        return restClient.get()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwtToken))
                .retrieve()
                .toBodilessEntity();
    }
}
