package yongsuchul.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class FlaskResponseDto {
    private Boolean success;
    private String video;

    @Override
    public String toString(){
//        StringBuilder result = new StringBuilder();
//        for(String i : this.name){
//            result.append(", ").append(i);
//        }
        return video;
    }
}
