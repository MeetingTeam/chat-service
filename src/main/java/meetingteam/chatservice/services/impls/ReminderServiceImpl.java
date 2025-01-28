package meetingteam.chatservice.services.impls;

import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.models.enums.MessageType;
import meetingteam.chatservice.repositories.MessageRepository;
import meetingteam.chatservice.services.ReminderService;
import meetingteam.chatservice.utils.WebsocketUtil;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import meetingteam.commonlibrary.utils.AuthUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {
    private final MessageRepository messageRepo;
    private final WebsocketUtil websocketUtil;

    public void handleMessage(Message message){
        if(message.getReminder()==null)
            throw new BadRequestException("Reminder is required when creating reminder message");
    }

    public void closeReminder(String messageId, String nickNam){
        var message=messageRepo.findById(messageId).orElseThrow(()->new BadRequestException("Message not found"));

        if(message.getType() != MessageType.REMINDER)
            throw new BadRequestException("This is not reminder message");

        var userId= AuthUtil.getUserId();
        if(!message.getSenderId().equals(userId))
            throw new BadRequestException("You are not allowed to close this vote");



        messageRepo.save(message);
        websocketUtil.broadcastMessage(message);
    }
}
