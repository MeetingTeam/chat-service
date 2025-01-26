package meetingteam.chatservice.dtos.MediaFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMediaFile {
    @NotBlank
    private String fileName;

    @NotBlank
    private String fileType;

    @Min(1)
    private Integer fileSizeInBytes;
}
