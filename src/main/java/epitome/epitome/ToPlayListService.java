package epitome.epitome;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class ToPlayListService {

    private final Map<String, String> genrePlaylistMap;

    // 장르와 playlistId를 미리 설정
    public ToPlayListService() {
        genrePlaylistMap = new HashMap<>();
        genrePlaylistMap.put("Hip-Pop", "playlistId1");
        genrePlaylistMap.put("Electronic", "playlistId2");
        genrePlaylistMap.put("Rock", "playlistId3");
        genrePlaylistMap.put("Instrumental", "playlistId4");
        genrePlaylistMap.put("International", "playlistId5");
        genrePlaylistMap.put("Experimental", "playlistId6");
        genrePlaylistMap.put("Pop", "playlistId7");
        genrePlaylistMap.put("Folk", "playlistId8");
    }

    // 주어진 장르에 해당하는 playlistId를 반환
    public String getPlaylistIdByGenre(String genre) {
        return genrePlaylistMap.getOrDefault(genre, "defaultPlaylistId"); // 장르가 없으면 기본 ID를 반환
    }
}