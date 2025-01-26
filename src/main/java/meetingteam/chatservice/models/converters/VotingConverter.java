package meetingteam.chatservice.models.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import meetingteam.chatservice.models.Voting;
import meetingteam.commonlibrary.exceptions.InternalServerException;

public class VotingConverter implements AttributeConverter<Voting, String> {
    private final ObjectMapper objectMapper=new ObjectMapper().findAndRegisterModules();

    @Override
    public String convertToDatabaseColumn(Voting attribute) {
        try {
            String dbData=null;
            if(attribute!=null)
                dbData=objectMapper.writeValueAsString(attribute);
            return dbData;
        } catch (Exception e) {
            throw new InternalServerException("Unable to convert Voting to JSON");
        }
    }

    @Override
    public Voting convertToEntityAttribute(String dbData) {
        try {
            Voting voting=null;
            if(dbData!=null)
                voting=objectMapper.readValue(dbData,new TypeReference<>(){});
            return voting;
        } catch (Exception e) {
            throw new InternalServerException("Unable to convert JSON string to Voting");
        }
    }
}
