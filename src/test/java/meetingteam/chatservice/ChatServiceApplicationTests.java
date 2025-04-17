package meetingteam.chatservice;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import meetingteam.chatservice.dtos.Voting.ChooseOptionDto;

@SpringBootConfiguration
class ChatServiceApplicationTests {
    private final Validator validator;

    public ChatServiceApplicationTests() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }
    
    @Test
    void contextLoads() {
        var optionDto = new ChooseOptionDto();
        
        Set<ConstraintViolation<ChooseOptionDto>> violations = validator.validate(optionDto);
        assertTrue(!violations.isEmpty(), "DTO should be invalid");
    }
}