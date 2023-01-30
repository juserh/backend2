package yongsuchul.backend.cotroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yongsuchul.backend.model.Category;
import yongsuchul.backend.model.Photobook;
import yongsuchul.backend.model.User;
import yongsuchul.backend.service.PhotobookService;
import yongsuchul.backend.service.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path="/photobooks")
public class PhotobookController {

    @Autowired
    PhotobookService photobookService;
    @Autowired
    UserService userService;

    @PostMapping(value = "/save")//@RequestBody
    public HashMap<String, Object> savePhotobookData(@RequestPart Photobook photobook, @RequestPart("files")List<MultipartFile> files){//@RequestPart("files")List<MultipartFile> files
        HashMap<String, Object> map = new HashMap<>();
        try{
            photobookService.savePhotobook(photobook, files);
            map.put("success", Boolean.TRUE);
            return map;
        }catch(Exception e){
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
            return map;
        }
    }

    @GetMapping(value="/{userid}")
    public HashMap<String, Object> getMyPhotobooks(@PathVariable String userid){
        HashMap<String, Object> map = new HashMap<>();
        try{
            List<Photobook> my=photobookService.findMyPhotobook(userid);
            map.put("success", Boolean.TRUE);
            map.put("photobooks", my);
            map.put("categories", photobookService.findAllCategoryNames());
        }catch(Exception e){
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    @GetMapping(value="/{photobookid}/detail")
    public HashMap<String, Object> getPhotobookDetail(@PathVariable String photobookid){
        HashMap<String, Object> map = new HashMap<>();
        try{
            Optional<Photobook> photobook= photobookService.findPhotobookDetail(photobookid);
            map.put("success", Boolean.TRUE);
            map.put("photobook", photobook);
        }catch(Exception e){
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    @GetMapping(value="/{photobookid}/delete")
    public HashMap<String, Object> deletePhotobook(@PathVariable String photobookid){
        HashMap<String, Object> map = new HashMap<>();
        try{
            photobookService.deletePhotobook(photobookid);
            map.put("success", Boolean.TRUE);
        }catch(Exception e){
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }
        return map;
    }

    @PostMapping(value = "/{photobookid}/update")
    public HashMap<String, Object> updatePhotobook(@PathVariable String photobookid, @RequestBody Photobook photobook){
        HashMap<String, Object> map = new HashMap<>();
        try{
            photobookService.updatePhotobook(photobook);
            map.put("success",Boolean.TRUE);
        }catch(Exception e){
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }
        return map;
    }

    @PostMapping(value = "/{photobookid}/invite")
    public HashMap<String, Object> inviteEditors(@PathVariable String photobookid, @RequestBody List<User> editors){
        HashMap<String, Object> map = new HashMap<>();
        try{
//            photobookService.findPhotobookDetail(photobookid).ifPresent(p-> {
//                p.setEditors(editors);
//                photobookService.updatePhotobook(p.getId().toString(),p);
//            });
            photobookService.inviteEditors(photobookid, editors);

            map.put("success",Boolean.TRUE);
        }catch(Exception e){
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }
        return map;
    }

    @GetMapping(value = "/public")
    public HashMap<String, Object> showPublicPhotobooks() {
        HashMap<String, Object> map = new HashMap<>();

        try {
            List<Photobook> photobooks = photobookService.findAllPublicPhotobooks();
            map.put("success", Boolean.TRUE);
            map.put("photobooks", photobooks);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }
        return map;
    }
}
