package meetingteam.chatservice.dtos.MediaFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePresignedUrlDto {
    private String fileName;

    private String fileType;

    @Min(1) @Max(100000000)
    private Long fileSize;
}
