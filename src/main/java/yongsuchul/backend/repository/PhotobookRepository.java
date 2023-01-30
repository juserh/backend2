package yongsuchul.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yongsuchul.backend.model.Photobook;
import yongsuchul.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface PhotobookRepository extends MongoRepository<Photobook, String> {
    List<Photobook> findAllByEditors(Optional<User> editor); //특정 user가 포함된 editors 배열을 가진 포토북 찾아오기
}
