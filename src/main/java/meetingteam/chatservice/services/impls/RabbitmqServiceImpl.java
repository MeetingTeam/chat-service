package meetingteam.chatservice.services.impls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.constraints.WebsocketTopics;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.services.RabbitmqService;
import meetingteam.commonlibrary.dtos.SocketDto;
import meetingteam.commonlibrary.exceptions.InternalServerException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitmqServiceImpl implements RabbitmqService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper=new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Value("${rabbitmq.exchange-name}")
    private String exchangeName;

    @Override
    public void sendToUser(String userId, String topic, Object payload) {
        try{
            String dest="/topic/user."+userId;
            SocketDto socketDto = new SocketDto(dest, topic, payload);
            String jsonData = objectMapper.writeValueAsString(socketDto);
            rabbitTemplate.convertAndSend(exchangeName,dest, jsonData);
        }
        catch(Exception e){
            throw new InternalServerException("Unable to send message");
        }
    }

    public void sendToTeam(String teamId, String topic, Object payload){
        try{
            String dest= "/topic/team."+teamId;
            SocketDto socketDto = new SocketDto(dest,topic, payload);
            String jsonData = objectMapper.writeValueAsString(socketDto);
            rabbitTemplate.convertAndSend(exchangeName, dest, jsonData);
        }
        catch(Exception e){
            throw new InternalServerException("Unable to send message");
        }
    }
}
