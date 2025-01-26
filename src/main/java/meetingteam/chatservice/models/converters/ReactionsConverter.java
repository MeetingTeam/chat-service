package meetingteam.chatservice.models.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import meetingteam.chatservice.models.Reaction;
import meetingteam.commonlibrary.exceptions.InternalServerException;

import java.util.List;

public class ReactionsConverter implements AttributeConverter<List<Reaction>, String> {
	private final ObjectMapper objectMapper=new ObjectMapper().findAndRegisterModules();

	@Override
	public String convertToDatabaseColumn(List<Reaction> attribute) {
		try {	
			String dbData="[]";
			if(attribute!=null && !attribute.isEmpty())
				dbData=objectMapper.writeValueAsString(attribute);
			return dbData;
		} catch (Exception e) {
			throw new InternalServerException("Unable to convert reactions to JSON");
		}
	}
	
	@Override
	public List<Reaction> convertToEntityAttribute(String dbData) {
		try {
			List<Reaction> reactions=null;
			if(dbData!=null)
				reactions=objectMapper.readValue(dbData,new TypeReference<>(){});
			return reactions;
		} catch (Exception e) {
			throw new InternalServerException("Unable to convert JSON string to messageReactions");
		}
	}
}

