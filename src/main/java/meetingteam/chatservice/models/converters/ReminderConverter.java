package meetingteam.chatservice.models.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import meetingteam.chatservice.models.Reminder;
import meetingteam.commonlibrary.exceptions.InternalServerException;

public class ReminderConverter implements AttributeConverter<Reminder, String> {
    private final ObjectMapper objectMapper=new ObjectMapper().findAndRegisterModules();

    @Override
    public String convertToDatabaseColumn(Reminder attribute) {
        try {
            String dbData=null;
            if(attribute!=null)
                dbData=objectMapper.writeValueAsString(attribute);
            return dbData;
        } catch (Exception e) {
            throw new InternalServerException("Unable to convert Reminder to JSON");
        }
    }

    @Override
    public Reminder convertToEntityAttribute(String dbData) {
        try {
            Reminder reminder=null;
            if(dbData!=null)
                reminder=objectMapper.readValue(dbData,new TypeReference<>(){});
            return reminder;
        } catch (Exception e) {
            throw new InternalServerException("Unable to convert JSON string to Reminder");
        }
    }
}
