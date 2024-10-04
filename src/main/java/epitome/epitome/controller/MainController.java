package epitome.epitome.controller;

import epitome.epitome.model.Track;
import epitome.epitome.services.FindTrackService;
import epitome.epitome.services.GenreClassificationService;
import epitome.epitome.services.SimilarityClassificationService;
import epitome.epitome.services.ToPlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/music")
public class MainController {

    private final FindTrackService findTrackService;
    private final GenreClassificationService genreClassificationService;
    private final ToPlayListService toPlayListService;
    private final SimilarityClassificationService similarityClassificationService;

    @Autowired
    public MainController(FindTrackService findTrackService, GenreClassificationService genreClassificationService,
                          ToPlayListService toPlayListService, SimilarityClassificationService similarityClassificationService) {
        this.findTrackService = findTrackService;
        this.genreClassificationService = genreClassificationService;
        this.toPlayListService = toPlayListService;
        this.similarityClassificationService = similarityClassificationService;
    }

    @PostMapping("/upload")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Track>> listTracks(@RequestParam("file") MultipartFile file) {

        try {
            //음악 원본 파일을 바이트 배열로 변경
            byte[] musicFileBytes = file.getBytes();

            //장르 분류 서비스 사용
            //String genre = genreClassificationService.classifyGenre(musicFileBytes);

            // 트랙 정보를 검색하고 반환(장르의 해당하는 id로 변경)
            String playlistId = toPlayListService.getPlaylistIdByGenre("Pop");
            List<Track> tracks = findTrackService.searchTrack(playlistId);
            /*
            //유사도 분류 서비스 사용
            Double[] similariy = similarityClassificationService.classifySimilarity(musicFileBytes,tracks);
            //tracks 에 유사도 추가

            for(int i=0;i<similariy.length;i++){
                tracks.get(i).setSimilarity(similariy[i]);
            }
            */
            return ResponseEntity.ok(tracks);
        } catch (IOException e) {
            // 예외 처리: 파일 저장 중 오류 발생 시
            System.err.println("파일 저장 오류: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }

    }


}