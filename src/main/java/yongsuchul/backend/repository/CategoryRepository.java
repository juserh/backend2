package yongsuchul.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yongsuchul.backend.model.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findByName(String name);
}
