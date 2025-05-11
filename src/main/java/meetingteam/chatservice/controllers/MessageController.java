package meetingteam.chatservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.dtos.Message.CreateTextMessageDto;
import meetingteam.chatservice.dtos.Message.CreateReactionDto;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/text_message")
    public ResponseEntity<Message> receiveTextMessage(
            @RequestBody @Valid CreateTextMessageDto messageDto){
        messageService.receiveTextMessage(messageDto);
        return ResponseEntity.ok().build();
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

    @GetMapping("/friend/{friendId}")
    public ResponseEntity<List<Message>> getFriendMessages(
            @RequestParam Integer receivedMessageNum,
            @PathVariable String friendId){
        return ResponseEntity.ok(messageService.getFriendMessages(receivedMessageNum,friendId));
    }

    @GetMapping("/text_channel/{channelId}")
    public ResponseEntity<List<Message>> getTextChannelMessages(
            @RequestParam Integer receivedMessageNum,
            @PathVariable String channelId){
        return ResponseEntity.ok(messageService.getTextChannelMessages(receivedMessageNum,channelId));
    }

    @DeleteMapping("/private/channel/{channelId}")
    public ResponseEntity<Void> deleteMessagesByChannelId(
            @PathVariable("channelId") String channelId){
        messageService.deleteMessagesByChannelId(channelId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/private/team/{teamId}")
    public ResponseEntity<Void> deleteMessagesByTeamId(
            @PathVariable("teamId") String teamId){
        messageService.deleteMessagesByTeamId(teamId);
        return ResponseEntity.ok().build();
    }
}
