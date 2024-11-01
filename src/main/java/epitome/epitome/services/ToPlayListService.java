package epitome.epitome.services;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class ToPlayListService {

    private final Map<String, String> genrePlaylistMap;

    // 장르와 playlistId를 미리 설정
    public ToPlayListService() {
        genrePlaylistMap = new HashMap<>();
        genrePlaylistMap.put("Hip-Pop", "0BwGaGFGYCGpSI3BWFnFQZ");
        genrePlaylistMap.put("Electronic", "7ioVxAwaaEJgjTOlkWoGTE");
        genrePlaylistMap.put("Rock", "2kf2UuHxJ5YBU1qJUab7Kc");
        genrePlaylistMap.put("Instrumental", "4Mbe4JD8i0nn9wsJDXiw3g");
        genrePlaylistMap.put("International", "7MMOK7hwhd2xUeXttkZhTT");
        genrePlaylistMap.put("Experimental", "0BQyQOeez75wN64tG2kTLl");
        genrePlaylistMap.put("Pop", "78LHzws4XXFYgXQPBApYlr");
        genrePlaylistMap.put("Folk", "7n1kAASte9LelSVcNf2axY");
        genrePlaylistMap.put("qwer","42k95wSSLjKQT8Vp1aXwzP");
    }

    // 주어진 장르에 해당하는 playlistId를 반환
    public String getPlaylistIdByGenre(String genre) {
        return genrePlaylistMap.getOrDefault(genre, "defaultPlaylistId"); // 장르가 없으면 기본 ID를 반환
    }
}