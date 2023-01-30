package yongsuchul.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yongsuchul.backend.model.User;
import yongsuchul.backend.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Component
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Boolean duplicateInspection(String id, String nickname) {
        if (!(userRepository.findById(id).isPresent())
                && (userRepository.findByNickname(nickname) == null)) {
            return Boolean.TRUE;
        }
        else { return Boolean.FALSE; }
    }

    public User findByNickname(String nickname){
        return userRepository.findByNickname(nickname);
    }

    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }

    public void changePwd(User user, String newPwd) {
        user.setPassword(newPwd);
        userRepository.save(user);
    }

    public String selectUser(String nickname){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            if(userRepository.findByNickname(nickname) == null) {
                log.info("[Service] user nickname: {} not exist", nickname);
                return String.format("user nickname: %s not exist", nickname);
            } else{
                return objectMapper.writeValueAsString(userRepository.findByNickname(nickname));
            }
        }  catch (JsonProcessingException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public User saveUser(String id, String password, String nickname){
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setNickname(nickname);

        if(userRepository.findByNickname(nickname) != null){
            log.info("[Service][update] name is already exist");
//            user.setId(userRepository.findByNickname(nickname).getId());
            throw new IllegalStateException("이미 존재하는 닉네임 입니다.");
        }
        if(userRepository.findById(id).isPresent()){
            log.info("[Service][회원가입 실패] id is already exist");
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        userRepository.save(user);
        log.info("[Service][회원가입 성공] New name received!");
        return user;
    }
}
