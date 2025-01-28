package meetingteam.chatservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.dtos.Message.CreateMessageDto;
import meetingteam.chatservice.dtos.Message.CreateReactionDto;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> receiveMessage(
            @RequestBody @Valid CreateMessageDto messageDto){
        return ResponseEntity.ok(messageService.receiveMessage(messageDto));
    }

    @PutMapping("/reaction")
    public ResponseEntity<Message> reactMessage(
            @RequestBody @Valid CreateReactionDto reactionDto){
        return ResponseEntity.ok(messageService.reactMessage(reactionDto.getMessageId(), reactionDto.getEmojiCode()));
    }

    @PutMapping("/unsend/{messageId}")
    public ResponseEntity<Message> unsendMessage(
            @PathVariable("messageId") String messageId
    ){
        return ResponseEntity.ok(messageService.unsendMessage(messageId));
    }

    @GetMapping("/private")
    public ResponseEntity<List<Message>> getPrivateMessages(
            @RequestParam("receivedMessageNum") Integer receivedMessageNum,
            @RequestParam("friendId") String friendId){
        return ResponseEntity.ok(messageService.getPrivateMessages(receivedMessageNum,friendId));
    }

    @GetMapping("/text_channel")
    public ResponseEntity<List<Message>> getTextChannelMessages(
            @RequestParam("receivedMessageNum") Integer receivedMessageNum,
            @RequestParam("channelId") String channelId){
        return ResponseEntity.ok(messageService.getTextChannelMessages(receivedMessageNum,channelId));
    }

    @DeleteMapping("/private/channel/{channelId}")
    public ResponseEntity<List<Message>> getTextChannelMessages(
            @PathVariable("channelId") String channelId){
        messageService.deleteMessagesByChannelId(channelId);
        return ResponseEntity.ok().build();
    }
}
