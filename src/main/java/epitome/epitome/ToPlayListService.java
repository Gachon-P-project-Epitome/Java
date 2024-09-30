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
        genrePlaylistMap.put("Pop", "playlistId1");
        genrePlaylistMap.put("Rock", "playlistId2");
        genrePlaylistMap.put("Jazz", "playlistId3");
        genrePlaylistMap.put("HipHop", "playlistId4");
        genrePlaylistMap.put("Classical", "playlistId5");
        genrePlaylistMap.put("Country", "playlistId6");
        genrePlaylistMap.put("EDM", "playlistId7");
        genrePlaylistMap.put("Reggae", "playlistId8");
    }

    // 주어진 장르에 해당하는 playlistId를 반환
    public String getPlaylistIdByGenre(String genre) {
        return genrePlaylistMap.getOrDefault(genre, "defaultPlaylistId"); // 장르가 없으면 기본 ID를 반환
    }
}