package yongsuchul.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yongsuchul.backend.model.Music;

@Repository

public interface MusicRepository extends MongoRepository<Music, String> {
    Music findByTitle(String title);
}
