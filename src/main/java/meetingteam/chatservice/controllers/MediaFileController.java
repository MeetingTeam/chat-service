package meetingteam.chatservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.dtos.MediaFile.CreatePresignedUrlDto;
import meetingteam.chatservice.dtos.Message.CreateFileMessageDto;
import meetingteam.chatservice.services.MediaFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/media-file")
@PreAuthorize("isAuthenticated()")
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
