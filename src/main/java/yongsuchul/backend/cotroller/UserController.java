package yongsuchul.backend.cotroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yongsuchul.backend.AuthInfo;
import yongsuchul.backend.model.Photobook;
import yongsuchul.backend.model.User;
import yongsuchul.backend.repository.PhotobookRepository;
import yongsuchul.backend.service.AuthService;
import yongsuchul.backend.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api")
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private final PhotobookRepository photobookRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "users/loginPage";
    }

    @PostMapping("/login")
    public HashMap<String, Object> login(@RequestParam String id, @RequestParam String password, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            AuthInfo authInfo = authService.authenticate(id, password);
            session.setAttribute("authInfo", authInfo);

            map.put("success", Boolean.TRUE);
            map.put("nickname", authInfo.getNickname());
            map.put("authInfo", authInfo);
            return map;
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("message", e.getMessage());
            return map;
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "users/registerPage";
    }

    @PostMapping("/register")
    public HashMap<String, Object> register(@RequestParam String id, @RequestParam String password, @RequestParam String nickname) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            User user = userService.saveUser(id, password, nickname);

            map.put("success", Boolean.TRUE);
            map.put("id", user.getId());
            map.put("password", user.getPassword());
            map.put("nickname", user.getNickname());

            log.info("[Controller][회원가입 성공] id: {}, password: {}, nickname: {}", id, password, nickname);
            return map;
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("message", e.getMessage());
            return map;
        }
    }

    @PostMapping("/logout")
    public HashMap<String, Object> logout(HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();

        session.invalidate();
        authService.invalidate();

        log.info("[Controller][로그아웃]");
        map.put("success", Boolean.TRUE);
        map.put("authInfo", authService.getAuthInfo());
        return map;
    }

    @PutMapping("/changePwd")
    public HashMap<String, Object> changePwd(@RequestParam String currentPwd, @RequestParam String newPwd, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();

        try {
//            AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
            AuthInfo authInfo = authService.getAuthInfo();
            User user = userService.findByNickname(authInfo.getNickname());

            if (!user.getPassword().equals(currentPwd)) {
                throw new IllegalStateException("비밀번호가 일치하지않습니다.");
            }
            if (currentPwd.equals(newPwd)) {
                throw new IllegalStateException("동일한 비밀번호로 변경할 수 없습니다.");
            }

            userService.changePwd(user, newPwd);
            map.put("success", Boolean.TRUE);
            map.put("id", user.getId());
            map.put("nickname", user.getNickname());
            map.put("password", user.getPassword());
            return map;
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("message", e.getMessage());
            return map;
        }
    }

    @GetMapping("/{id}/myPage")
    public HashMap<String, Object> myPage(@PathVariable String id, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();

        try{
//            AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
            User user = userService.findById(id).get();

            List<Photobook> photobooks = photobookRepository.findAllByEditors(Optional.ofNullable(user));

            map.put("success", Boolean.TRUE);
            map.put("user", user);
            map.put("photobooks", photobooks);
            return map;
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("message", e.getMessage());
            return map;
        }

    }

}
