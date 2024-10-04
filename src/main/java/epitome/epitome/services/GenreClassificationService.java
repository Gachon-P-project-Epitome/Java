package epitome.epitome.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.ByteArrayResource;

@Service
public class GenreClassificationService {

    private RestTemplate restTemplate;

    public GenreClassificationService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public String classifyGenre(byte[] musicFileBytes){
        String flaskApiUrl = ""; // 플라스크 서버1 url

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 음악 파일을 2진데이터로 변경
        ByteArrayResource musicResource = new ByteArrayResource(musicFileBytes) {
            @Override
            public String getFilename() {
                return "music.mp3";
            }
        };
        HttpEntity<ByteArrayResource> requestEntity = new HttpEntity<>(musicResource, headers);

        String genre = restTemplate.postForObject(flaskApiUrl, requestEntity, String.class);

        return genre;
    }

}
