package meetingteam.chatservice.repositories.impls;

import lombok.RequiredArgsConstructor;
import meetingteam.chatservice.models.Message;
import meetingteam.chatservice.models.enums.MessageType;
import meetingteam.chatservice.repositories.MessageRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {
    private final MongoTemplate mongoTemplate;
    private final String CHANNEL_ID = "channelId";

    @Override
    public Message save(Message message) {
        return mongoTemplate.save(message);
    }

    @Override
    public Optional<Message> findById(String id) {
        return Optional.of(mongoTemplate.findById(id, Message.class));
    }

    @Override
    public void delete(Message message) {
        mongoTemplate.remove(message);
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query()
                .addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Message.class);
    }

    @Override
    public List<Message> getGroupMessages(String channelId, Pageable pageable) {
        Query query = new Query()
                .addCriteria(Criteria.where(CHANNEL_ID).is(channelId))
                .with(pageable) // Apply pagination
                .with(Sort.by(Sort.Direction.DESC, "createdAt")); // Sort by createdAt in descending order
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> getPrivateMessages(String userId1, String userId2, Pageable pageable) {
        Query query = new Query()
                .addCriteria(new Criteria().orOperator(
                        Criteria.where("senderId").is(userId1).and("recipientId").is(userId2),
                        Criteria.where("senderId").is(userId2).and("recipientId").is(userId1)
                ))
                .with(pageable)
                .with(Sort.by(Sort.Direction.DESC, "createdAt")); // Sort by createdAt in descending order
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> getFileMessagesByChannelId(String channelId) {
        Query query = new Query()
                .addCriteria(Criteria.where(CHANNEL_ID).is(channelId)
                        .and("type").in(Arrays.asList(
                                MessageType.AUDIO,
                                MessageType.IMAGE,
                                MessageType.VIDEO,
                                MessageType.DOCUMENT
                        )));
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public List<Message> getFileMessagesByTeamId(String teamId) {
        Query query = new Query()
                .addCriteria(Criteria.where("teamId").is(teamId)
                        .and("type").in(Arrays.asList(
                                MessageType.AUDIO,
                                MessageType.IMAGE,
                                MessageType.VIDEO,
                                MessageType.DOCUMENT
                        )));
        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public void deleteByChannelId(String channelId) {
        Query query = new Query()
                .addCriteria(Criteria.where(CHANNEL_ID).is(channelId));
        mongoTemplate.remove(query, Message.class);
    }

    @Override
    public void deleteByTeamId(String teamId) {
        Query query = new Query()
                .addCriteria(Criteria.where("teamId").is(teamId));
        mongoTemplate.remove(query, Message.class);
    }
}
