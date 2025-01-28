package meetingteam.chatservice.services;

import meetingteam.chatservice.models.MediaFile;
import meetingteam.chatservice.models.Message;

import java.util.List;

public interface MediaFileService {
    void handleFileMessage(Message message);
    String generatePreSignedUrl(String fileName);
    void deleteMediaFile(MediaFile mediaFile);
    void deleteMediaFiles(List<MediaFile> mediaFiles);
}
