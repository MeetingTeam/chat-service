package meetingteam.chatservice.models;

import lombok.Data;

@Data
public class MediaFile {
    private String fileName;

    private String fileType;

    private String fileUrl;

    private Integer fileSizeInBytes;
}
