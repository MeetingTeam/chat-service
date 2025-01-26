package meetingteam.chatservice.models.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import meetingteam.chatservice.models.MediaFile;
import meetingteam.chatservice.models.Voting;
import meetingteam.commonlibrary.exceptions.InternalServerException;

public class MediaFileConverter implements AttributeConverter<MediaFile, String> {
    private final ObjectMapper objectMapper=new ObjectMapper().findAndRegisterModules();

    @Override
    public String convertToDatabaseColumn(MediaFile attribute) {
        try {
            String dbData=null;
            if(attribute!=null)
                dbData=objectMapper.writeValueAsString(attribute);
            return dbData;
        } catch (Exception e) {
            throw new InternalServerException("Unable to convert MediaFile to JSON");
        }
    }

    @Override
    public MediaFile convertToEntityAttribute(String dbData) {
        try {
            MediaFile mediaFile=null;
            if(dbData!=null)
                mediaFile=objectMapper.readValue(dbData,new TypeReference<>(){});
            return mediaFile;
        } catch (Exception e) {
            throw new InternalServerException("Unable to convert JSON string to MediaFile");
        }
    }
}
