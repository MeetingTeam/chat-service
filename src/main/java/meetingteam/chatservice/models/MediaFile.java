package meetingteam.chatservice.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class MediaFile {
    private String fileName;

    private String fileType;

    private String fileUrl;

    private Integer fileSizeInBytes;
}
