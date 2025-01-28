package meetingteam.chatservice.dtos.Voting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ChooseOptionDto {
    @NotBlank
    private String messageId;

    @NotNull
    private List<String> optionNames;

    @NotBlank
    private String nickName;
}
