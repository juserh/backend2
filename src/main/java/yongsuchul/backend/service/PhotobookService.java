package yongsuchul.backend.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import yongsuchul.backend.dto.FlaskResponseDto;
import yongsuchul.backend.model.Photobook;
import yongsuchul.backend.model.User;
import yongsuchul.backend.model.Category;
import yongsuchul.backend.repository.CategoryRepository;
import yongsuchul.backend.repository.MusicRepository;
import yongsuchul.backend.repository.PhotobookRepository;
import yongsuchul.backend.repository.UserRepository;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhotobookService {
    @Autowired
    PhotobookRepository photobookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MusicRepository musicRepository;
    CategoryRepository categoryRepository;

    //s3
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
//    @Value("${flask.url}")
//    private final String url;


    public void savePhotobook(Photobook photobook, List<MultipartFile> files) throws JsonProcessingException {
        //List<String> contentTypes = new ArrayList<String>(); //파일 형식 리스트
        //List<String> filenames= new ArrayList<String>();

        List<String> urls=new ArrayList<String>();

        String url="http://localhost:5000/api/videoEdit";

        for(MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String contentType = "";

            switch (file.getOriginalFilename().split("\\.")[1]) {
                case "jpeg":
                    contentType = "image/jpeg";
                    break;
                case "png":
                    contentType = "image/png";
                    break;
            }
            try {
                ObjectMetadata meta = new ObjectMetadata();
                meta.setContentType(contentType);

                try {
                    amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), meta).withCannedAcl(CannedAccessControlList.PublicRead));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (AmazonServiceException e) {
                e.printStackTrace();
            } catch (SdkClientException e) {
                e.printStackTrace();
            }

            //s3에 올린 object 정보 가져오기
            ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(bucket);
            List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();

            urls.add(amazonS3.getUrl(bucket, fileName).toString());

        }

//        Photobook saved_photobook = new Photobook(photobook.getEditors(), photobook.getTitle(), photobook.getDescription());
//        saved_photobook.setPhotos(urls);
        photobook.setPhotos(urls);
        String id = photobookRepository.save(photobook).getId().toString();
        log.info(photobook.getPhotos().toString());


        //flask와 통신
        //get방식
//        musicRepository.findById(photobook.getMusic().getId()).ifPresent(m->{
//            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url)
//                    .queryParam("title", "flasktestvideo")
//                    .queryParam("nickname", "jh")
//                    .queryParam("photos",photobook.getPhotos())
//                    .queryParam("music", m.getUrl())
//                    .queryParam("lengths", m.getPoints())
//                    .build(false);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//            factory.setConnectTimeout(5000); // api 호출 타임아웃
//            factory.setReadTimeout(5000);   // api 읽기 타임아웃
//
//            RestTemplate restTemplate = new RestTemplate(factory);
//
//            ResponseEntity<FlaskResponseDto> response = restTemplate.getForEntity(uriComponents.toUriString(), FlaskResponseDto.class);

//            log.info(response.toString());
//
//        });

        //post방식
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("title", "flasktestvideo");
        body.add("nickname", "jh");
        body.add("photos", photobook.getPhotos().toString());
        musicRepository.findById(photobook.getMusic().getId()).ifPresent(m->{
            body.add("music",m.getUrl());
            body.add("lengths",m.getPoints().toString());
        });

        //messaage
        HttpEntity<?> requestMessage = new HttpEntity<>(body, httpHeaders);
        HttpEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class);

        // request
//        ResponseEntity<FlaskResponseDto> response = restTemplate.getForEntity(uriComponents.toUriString(), TrackingInfoDto.class);
//
        //response 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        FlaskResponseDto dto = objectMapper.readValue(response.getBody(), FlaskResponseDto.class);

        log.info(dto.getVideo());
    //}

    }

    public List<Photobook> findMyPhotobook(String userid){
        Optional<User> user = userRepository.findById(userid);
        return photobookRepository.findAllByEditors(user);
    }

    public Optional<Photobook> findPhotobookDetail(String id){
        Optional<Photobook> photobook = photobookRepository.findById(id);
        return photobook;
    }

    public List<Photobook> findAllPublicPhotobooks() {
        List<Photobook> all = photobookRepository.findAll();
        List<Photobook> photobooks = new ArrayList<Photobook>();
        for (Photobook book : all) {
            if (book.getShare() == Boolean.TRUE) {
                photobooks.add(book);
            }
        }
        return photobooks;
    }

    public void deletePhotobook(String id){
        photobookRepository.deleteById(id);
    }

    //수정하기: findAnModify?
    public void updatePhotobook(Photobook photobook){
        photobookRepository.save(photobook);
    }

    public void inviteEditors(String id, List<User> editors){
        photobookRepository.findById(id).ifPresent(p->{
            p.setEditors(editors);
            photobookRepository.save(p);
        });
    }

    public List<String> findAllCategoryNames() {
        List<Category> categories = categoryRepository.findAll();
        List<String> names = new ArrayList<String>();
        for (Category category : categories ) {
            names.add(category.getName());
        }
        return names;
    }
}
