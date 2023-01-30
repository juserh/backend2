package yongsuchul.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "musics")
public class Music {
    @Id
    private String id;
    private String title;
    private String singer;
    private String url; //s3 경로
    private List<Long> points; //편집점
    private Integer Time; //음악 총 길이
}
