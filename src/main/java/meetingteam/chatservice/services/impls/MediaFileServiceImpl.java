package meetingteam.chatservice.services.impls;

import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.dtos.MediaFile.CreateMediaFile;
import meetingteam.chatservice.models.MediaFile;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.services.MediaFileService;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaFileServiceImpl implements MediaFileService {
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    private Random rand = new Random();

    @Value("${s3.bucket-name}")
    private String bucketName;

    public void handleFileMessage(Message message) {
        if(message.getMediaFile()==null)
            throw new BadRequestException("MediaFile is required when creating file message");

        String fileName= message.getMediaFile().getFileName()+"_"+rand.nextInt(100000)+
                "."+message.getMediaFile().getFileType();
        String preSignedUrl= generatePreSignedUrl(fileName);

        message.getMediaFile().setFileName(fileName);
        message.getMediaFile().setFileUrl(preSignedUrl.split("\\?")[0]);
    }

    public String generatePreSignedUrl(String fileName){
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(r -> r
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(5)));
        return presignedRequest.url().toString();
    }

    public void deleteMediaFile(MediaFile mediaFile){
        DeleteObjectRequest deleteRequest= DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(mediaFile.getFileName())
                .build();
        s3Client.deleteObject(deleteRequest);
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
