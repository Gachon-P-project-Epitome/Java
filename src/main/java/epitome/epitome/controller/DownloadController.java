package epitome.epitome.controller;

import epitome.epitome.model.MusicTrack;
import epitome.epitome.services.AllTrackService;
import epitome.epitome.services.DownloadMp3;
import epitome.epitome.services.FindTrackService;
import epitome.epitome.services.ToPlayListService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class DownloadController {

    public final DownloadMp3 downloadMp3;
    public final FindTrackService findTrackService;
    public final ToPlayListService toPlayListService;
    public final AllTrackService allTrackService;

    public DownloadController(DownloadMp3 downloadMp3, FindTrackService findTrackService, ToPlayListService toPlayListService, AllTrackService allTrackService) {
        this.downloadMp3 = downloadMp3;
        this.findTrackService = findTrackService;
        this.toPlayListService = toPlayListService;
        this.allTrackService = allTrackService;
    }

    @GetMapping("/download")
    public String download() {
        String playlistId = toPlayListService.getPlaylistIdByGenre("Hip-Pop");

        List<MusicTrack> tracks = allTrackService.searchAll(playlistId);
        if (tracks.isEmpty()) {
            return "No tracks found";
        }
        Map<String,String> trackMap = new HashMap<String,String>();
        for (MusicTrack track : tracks) {
            trackMap.put(track.getPreviewUrl(),track.getId());
        }
        downloadMp3.downloadMultipleTracksAsMp3(trackMap);
        return "Download complete";
    }

}

