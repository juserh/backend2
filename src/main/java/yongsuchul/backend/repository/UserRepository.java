package yongsuchul.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yongsuchul.backend.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByNickname(String nickname);
}
