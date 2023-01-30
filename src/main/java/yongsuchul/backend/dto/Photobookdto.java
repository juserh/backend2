package yongsuchul.backend.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import yongsuchul.backend.model.Category;
import yongsuchul.backend.model.Music;
import yongsuchul.backend.model.Photobook;
import yongsuchul.backend.model.User;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class Photobookdto {
    private List<User> editors;
    private String title;
    private String description;
    private String mainPicture;
    private Music music;
    private Boolean share; //true 공유, false 비공유
    private Category category;
    private Date start_date;
    private Date end_date;

    public Photobookdto(List<User> editors, String title, String description, String mainPicture, Music music, Boolean share, Category category, Date start, Date end){
        this.editors=editors;
        this.title=title;
        this.description=description;
        this.mainPicture=mainPicture;
        this.music = music;
        this.share = share;
        this.category = category;
        this.start_date = start;
        this.end_date = end;
    }
    public Photobookdto(){};
    public Photobook toEntity(){
        return new Photobook(editors, title, description, mainPicture, music, share, category, start_date, end_date);
    }
}
