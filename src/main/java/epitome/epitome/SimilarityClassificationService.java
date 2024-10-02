package epitome.epitome;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SimilarityClassificationService {

    private RestTemplate restTemplate;

    public SimilarityClassificationService(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    

}
