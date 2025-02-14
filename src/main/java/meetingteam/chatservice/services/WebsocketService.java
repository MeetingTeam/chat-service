package meetingteam.chatservice.services;

import meetingteam.chatservice.models.Message;

public interface WebsocketService {
          void broadcastMessage(Message message);
}
