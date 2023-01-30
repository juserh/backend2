package yongsuchul.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yongsuchul.backend.AuthInfo;
import yongsuchul.backend.model.User;
import yongsuchul.backend.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Component
public class AuthService {

    @Autowired
    UserService userService;
    private AuthInfo authInfo;

    public AuthInfo authenticate(String id, String password) {
        Optional<User> user = userService.findById(id);

        if (!user.isPresent()) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }
        if (!user.get().getPassword().equals(password)) {
            log.info("[AuthService][로그인 실패] 원래 비밀번호: {}, 입력받은 비밀번호: {}", user.get().getPassword(), password);
            throw new IllegalStateException("비밀번호가 다릅니다.");
        }
        authInfo = new AuthInfo(user.get().getId(), user.get().getNickname());
        log.info("[AuthService][로그인 성공] AuthInfo 생성 성공: {} {}", authInfo.getId(), authInfo.getNickname() );
        return authInfo;
    }

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public void invalidate() { authInfo = null; }
}
