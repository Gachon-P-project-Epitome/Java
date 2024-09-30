package epitome.epitome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/music")
public class MainController {

    private final FindTrackService findTrackService;

    @Autowired
    public MainController(FindTrackService findTrackService) {
        this.findTrackService = findTrackService;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<Track>> listTracks(@RequestParam("file") MultipartFile file) {
        String directory = "/Users/사용자명/uploads";  // 저장할 폴더 경로
        Path path = Paths.get(directory, file.getOriginalFilename());

        try {
            // 파일을 루트 디렉토리에 저장
            file.transferTo(path.toFile()); // 파일 전송

            // 저장된 음악 파일 확인
            System.out.println("파일이 저장되었습니다: " + path.toString());

            // 트랙 정보를 검색하고 반환
            List<Track> tracks = findTrackService.searchTrack("37i9dQZF1DX3ZeFHRhhi7Y");
            return ResponseEntity.ok(tracks);
        } catch (IOException e) {
            // 예외 처리: 파일 저장 중 오류 발생 시
            System.err.println("파일 저장 오류: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }

    }


}