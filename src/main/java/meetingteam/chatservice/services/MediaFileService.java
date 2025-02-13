package meetingteam.chatservice.services;

import meetingteam.chatservice.dtos.MediaFile.CreatePresignedUrlDto;
import meetingteam.chatservice.dtos.Message.CreateFileMessageDto;
import meetingteam.chatservice.models.MediaFile;

import java.util.List;

public interface MediaFileService {
    String generateS3PresignedUrl(CreatePresignedUrlDto presignedUrlDto);
    void receiveFileMessage(CreateFileMessageDto messageDto);
    void deleteMediaFile(MediaFile mediaFile);
    void deleteMediaFiles(List<MediaFile> mediaFiles);
}
