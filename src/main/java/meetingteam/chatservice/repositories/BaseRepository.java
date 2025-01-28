package meetingteam.chatservice.repositories;

import java.util.Optional;

public interface BaseRepository<T,ID> {
    T save(T t);
    Optional<T> findById(ID id);
    void delete(T t);
    void deleteById(ID id);
}
