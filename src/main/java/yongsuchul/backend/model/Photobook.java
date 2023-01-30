package yongsuchul.backend.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="photobooks")
public class Photobook {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @DBRef
    private List<User> editors;
    private String title;
    private String description;
    private String mainPicture; // 표지 사진 경로

    //사진 url 주소들
    private List<String> photos;
    private String video;

    @DBRef
    @JsonSerialize(using = ToStringSerializer.class)
    private Music music;

    private Boolean share; //true 공유, false 비공유

    @DBRef
    @JsonSerialize(using = ToStringSerializer.class)
    private Category category;

    private Date start_date;
    private Date end_date;

    @CreatedDate
    private LocalDateTime created_at; //자동
    @LastModifiedDate
    private LocalDateTime updated_at; //자동

    public Photobook(List<User> editors, String title, String description, String mainPicture, Music music, Boolean share, Category category, Date start, Date end){
        this.editors=editors;
        this.title=title;
        this.description=description;
        this.mainPicture = mainPicture;
        this.music = music;
        this.share = share;
        this.category = category;
        this.start_date = start;
        this.end_date = end;
    }
}
