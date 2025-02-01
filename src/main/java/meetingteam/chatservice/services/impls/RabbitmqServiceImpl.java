package meetingteam.chatservice.services.impls;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper=new ObjectMapper().findAndRegisterModules();

    @Value("${rabbitmq.exchange-name}")
    private String exchangeName;

    @Override
    public void sendToUser(String userId, String topic, Object payload) {
        try{
            SocketDto socketDto = new SocketDto("user:"+topic, payload);
            String jsonData = objectMapper.writeValueAsString(socketDto);
            rabbitTemplate.convertAndSend(exchangeName, "user."+userId, jsonData);
        }
        catch(Exception e){
            throw new InternalServerException("Unable to send message");
        }
    }

    public void sendToTeam(String teamId, String topic, Object payload){
        try{
            String jsonPayload= objectMapper.writeValueAsString(payload);
            SocketDto socketDto = new SocketDto("team:"+topic, jsonPayload);
            rabbitTemplate.convertAndSend(exchangeName, "team."+teamId, socketDto);
        }
        catch(Exception e){
            throw new InternalServerException("Unable to send message");
        }
    }

    public void broadcastMessage(Message message) {
        if(message.getRecipientId()!=null){
            sendToUser(message.getSenderId(),  WebsocketTopics.AddOrUpdateMessage, message);
            sendToUser(message.getRecipientId(), WebsocketTopics.AddOrUpdateMessage, message);
        }
        else {
            sendToTeam(message.getTeamId(), WebsocketTopics.AddOrUpdateMessage, message);
        }
    }
}
