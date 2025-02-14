package meetingteam.chatservice.services.impls;

import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.dtos.MediaFile.CreatePresignedUrlDto;
import meetingteam.chatservice.dtos.Message.CreateFileMessageDto;
import meetingteam.chatservice.models.MediaFile;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.models.enums.MessageType;
import meetingteam.chatservice.repositories.MessageRepository;
import meetingteam.chatservice.services.*;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import meetingteam.commonlibrary.exceptions.InternalServerException;
import meetingteam.commonlibrary.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaFileServiceImpl implements MediaFileService {
    private final MessageRepository messageRepo;
    private final UserService userService;
    private final TeamService teamService;
    private final WebsocketService websocketService;
    private final ModelMapper modelMapper;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final Random rand = new Random();


    @Value("${s3.bucket-name}")
    private String bucketName;
    @Value("${s3.url}")
    private String s3BaseUrl;

    public String generateS3PresignedUrl(CreatePresignedUrlDto presignedUrlDto){
        String originFileName= presignedUrlDto.getFileName();
        String filename= originFileName.substring(0, originFileName.lastIndexOf("."));
        String filetype= originFileName.substring(originFileName.lastIndexOf(".")+1);
        String objectKey= filename+"_"+rand.nextInt(100000)+"."+filetype;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .tagging("isLinked=false")
                .contentType(presignedUrlDto.getFileType())
                .contentLength(presignedUrlDto.getFileSize())
                .build();
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(r -> r
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(15)));

        return presignedRequest.url().toString();
    }

    public void receiveFileMessage(CreateFileMessageDto messageDto) {
        var message = modelMapper.map(messageDto, Message.class);
        String userId= AuthUtil.getUserId();

        if(message.getRecipientId()!=null){
            if(!userService.isFriend(userId, message.getRecipientId()))
                throw new AccessDeniedException("You are not friend of the recipient");
            message.setChannelId(null);
        }
        else if(message.getChannelId()!=null){
            if(!teamService.isMemberOfTeam(userId, message.getTeamId(), message.getChannelId()))
                throw new AccessDeniedException("You are not member of the team");
        }
        else throw new BadRequestException("Either RecipientId or ChannelId must not be null");

        message.setSenderId(userId);
        message.setCreatedAt(LocalDateTime.now());

        var mediaFile = message.getMediaFile();
        if(!mediaFile.getFileUrl().startsWith(s3BaseUrl))
            throw new BadRequestException("Invalid Media file URL");
        mediaFile.setFileName(mediaFile.getFileUrl().substring(s3BaseUrl.length()));

        if(mediaFile.getFileType().startsWith("image"))
            message.setType(MessageType.IMAGE);
        else if(mediaFile.getFileType().startsWith("video"))
            message.setType(MessageType.VIDEO);
        else if(mediaFile.getFileType().startsWith("audio"))
            message.setType(MessageType.AUDIO);
        else message.setType(MessageType.DOCUMENT);

        var savedMessage=messageRepo.save(message);
        websocketService.broadcastMessage(savedMessage);
    }

    public void deleteMediaFile(MediaFile mediaFile){
        try{
            DeleteObjectRequest deleteRequest= DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(mediaFile.getFileName())
                    .build();
            s3Client.deleteObject(deleteRequest);
        }
        catch(Exception e){
            throw new InternalServerException("Unable to delete media file");
        }
    }

    public void deleteMediaFiles(List<MediaFile> mediaFiles){
        List<ObjectIdentifier> objectIdentifiers = mediaFiles.stream()
                .map(mediaFile -> ObjectIdentifier.builder()
                            .key(mediaFile.getFileName()).build())
                .collect(Collectors.toList());

        DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(Delete.builder().objects(objectIdentifiers).build())
                .build();

        s3Client.deleteObjects(deleteRequest);
    }
}
