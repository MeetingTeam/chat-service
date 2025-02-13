package meetingteam.chatservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.dtos.MediaFile.CreatePresignedUrlDto;
import meetingteam.chatservice.dtos.Message.CreateFileMessageDto;
import meetingteam.chatservice.services.MediaFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/media-file")
@RequiredArgsConstructor
public class MediaFileController {
    private final MediaFileService mediaFileService;

    @PostMapping("/presigned-url")
    public ResponseEntity<String> generateS3PresignedUrl(
            @RequestBody @Valid CreatePresignedUrlDto presignedUrlDto){
        return ResponseEntity.ok(
                mediaFileService.generateS3PresignedUrl(presignedUrlDto));
    }

    @PostMapping
    public ResponseEntity<Void> receiveFileMessage(
            @RequestBody @Valid CreateFileMessageDto messageDto){
        mediaFileService.receiveFileMessage(messageDto);
        return ResponseEntity.ok().build();
    }
}
