package meetingteam.chatservice.configs;

import io.opentelemetry.api.trace.Span;
import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.constraints.AnomalyTypes;
import meetingteam.chatservice.utils.AnomalyUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    private final AnomalyConfig anomalyConfig;

    @Bean
    public RestClient getRestClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor((request, body, execution)->{
                    for(var downService: anomalyConfig.downServicesList()) {
                        if(request.getURI().toString().contains(downService)) {
                            AnomalyUtil.markAnomalySpan(AnomalyTypes.DOWN_SERVICES);
                            break;
                        }
                    }
                    return execution.execute(request, body);
                })
                .build();
    }
}
