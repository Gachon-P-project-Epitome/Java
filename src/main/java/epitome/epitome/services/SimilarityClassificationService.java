package epitome.epitome.services;

import epitome.epitome.model.MusicTrack;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimilarityClassificationService {

    private RestTemplate restTemplate;

    public SimilarityClassificationService(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    public Double[] classifySimilarity(byte[] musicFileBytes,List<MusicTrack> tracks){

        String flaskApiUrl = ""; //플라스크서버 url;

        // preview url 추출
        List<String> previewUrls = new ArrayList<>();
        for(MusicTrack musicTrack :tracks){
            previewUrls.add(musicTrack.getPreviewUrl());
        }

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 음악 파일을 ByteArrayResource로 변경
        ByteArrayResource musicResource = new ByteArrayResource(musicFileBytes) {
            @Override
            public String getFilename() {
                return "music.mp3";  // 전송할 파일의 이름 설정
            }
        };

        // 여러 파트로 구성된 요청 본문 생성 (음악 파일 + preview URL 리스트)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", musicResource);
        body.add("previewUrls", previewUrls);

        // 헤더와 바디를 합침
        HttpEntity<MultiValueMap<String,Object>> requestEntity =new HttpEntity<>(body,headers);

        // Flask 서버에 POST 요청을 보내고, Double 배열 형태의 유사도 점수를 응답으로 받음
        Double[] response = restTemplate.postForObject(flaskApiUrl, requestEntity, Double[].class);

        return response;

    }

    

}
