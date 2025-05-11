package meetingteam.chatservice.services.impls;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.constraints.WebsocketTopics;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.services.RabbitmqService;
import meetingteam.chatservice.services.WebsocketService;

@Service
@RequiredArgsConstructor
public class WebsocketServiceImpl implements WebsocketService{
          private final RabbitmqService rabbitmqService;

          @Override
          public void broadcastMessage(Message message) {
                    if(message.getRecipientId()!=null){
                              rabbitmqService.sendToUser(message.getSenderId(),  WebsocketTopics.AddOrUpdateMessage, message);
                              rabbitmqService.sendToUser(message.getRecipientId(), WebsocketTopics.AddOrUpdateMessage, message);
                    }
                    else {
                              rabbitmqService.sendToTeam(message.getTeamId(), WebsocketTopics.AddOrUpdateMessage, message);
                    }  
          }   
}
