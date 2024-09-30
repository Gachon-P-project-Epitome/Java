package epitome.epitome.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SearchTrackFromPlayList {
    private final CreateToken createToken;

    @Autowired
    public SearchTrackFromPlayList(CreateToken createToken) {
        this.createToken = createToken;
    }

    // 플레이리스트에서 무작위로 트랙을 가져오는 메서드
    public void searchPlaylist(String playlistId, int limit) {
        String accessToken = createToken.accesstoken();
        if (accessToken == null) {
            System.out.println("Failed to get access token.");
            return;
        }

        // Spotify API 설정
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        // GetPlaylistRequest 생성
        GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(playlistId)
                .build();

        try {
            // 플레이리스트 정보 가져오기
            Playlist playlist = getPlaylistRequest.execute();
            System.out.println("플레이리스트 이름: " + playlist.getName());

            // 플레이리스트에서 모든 트랙 정보 가져오기
            Paging<PlaylistTrack> playlistTracks = playlist.getTracks();
            List<PlaylistTrack> trackList = new ArrayList<>();

            // 모든 트랙을 리스트에 추가
            Collections.addAll(trackList, playlistTracks.getItems());

            // 트랙 리스트를 무작위로 섞기
            Collections.shuffle(trackList);

            int trackCount = 0;

            // 무작위로 섞인 트랙 중에서 limit만큼 출력
            for (PlaylistTrack playlistTrack : trackList) {
                if (trackCount >= limit) {
                    break; // limit에 도달하면 루프 종료
                }

                Track track = (Track) playlistTrack.getTrack();
                if (track.getPreviewUrl() != null) {
                    // 미리듣기 가능한 트랙 출력
                    System.out.println("제목: " + track.getName());
                    System.out.println("가수: " + track.getArtists()[0].getName());
                    System.out.println("미리 듣기 URL: " + track.getPreviewUrl());
                } else {
                    System.out.println("미리듣기를 제공하지 않는 트랙: " + track.getName());
                }

                trackCount++; // 트랙 수 증가
            }

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
