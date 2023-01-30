package yongsuchul.backend.cotroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yongsuchul.backend.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path="/mongo")
public class UserTestController {

    @Autowired
    private final UserService userService;

    @GetMapping(value = "/find")
    public String findUserData(@RequestParam String nickname){
        return userService.selectUser(nickname);
    }

    @GetMapping(value = "/save")
    public String saveUserData(@RequestParam String id, @RequestParam String password, @RequestParam String nickname){
        log.info("[Contorller][Recv] id: {}, password: {}, nickname: {}", id, password, nickname);
        userService.saveUser(id, password, nickname);

        return userService.selectUser(nickname);
    }
}
