package meetingteam.chatservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.dtos.Voting.ChooseOptionDto;
import meetingteam.chatservice.services.VotingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voting")
@RequiredArgsConstructor
public class VotingController {
    private final VotingService votingService;

    @PostMapping("/choose-option")
    public ResponseEntity<Void> chooseOption(
            @RequestBody @Valid ChooseOptionDto optionDto){
        votingService.chooseOption(optionDto.getMessageId(), optionDto.getOptionNames(),
                optionDto.getNickName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/block/{messageId}")
    public ResponseEntity<Void> blockVoting(
            @PathVariable("messageId") String messageId,
            @RequestParam("nickName") String nickName){
        votingService.blockVoting(messageId, nickName);
        return ResponseEntity.ok().build();
    }
}
