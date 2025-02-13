package meetingteam.chatservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaFile {
    private String fileName;

    private String fileType;

    private String fileUrl;

    private Long fileSizeInBytes;
}
