package yongsuchul.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection="users") //mongodb collection
public class User {
    @Id
    private String id;
    private String password;
    private String nickname;
    //private String pic;
}
