package meetingteam.chatservice.services;

import meetingteam.chatservice.models.Message;

public interface ReminderService {
    void handleMessage(Message message);
}
