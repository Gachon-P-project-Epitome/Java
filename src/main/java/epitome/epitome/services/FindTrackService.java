package epitome.epitome.services;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import epitome.epitome.spotify.CreateToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FindTrackService {
    private final CreateToken createToken;

    @Autowired
    public FindTrackService(CreateToken createToken) {
        this.createToken = createToken;
    }

    public List<epitome.epitome.model.Track> searchTrack(String playlistId) {
        String accessToken = createToken.accesstoken();
        if (accessToken == null) {
            System.out.println("Failed to get access token.");
            return Collections.emptyList();
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
            Collections.addAll(trackList, playlistTracks.getItems());

            // 트랙 리스트를 무작위로 섞기
            Collections.shuffle(trackList);

            // 필요한 트랙 데이터만 list로 만들기
            List<epitome.epitome.model.Track> validTracks = new ArrayList<>();
            for (PlaylistTrack playlistTrack : trackList) {
                if (validTracks.size() >= 10) {
                    break; // 이미 10개의 트랙을 찾았다면 종료
                }

                // Spotify epitome.epitome.model.Track 가져오기
                com.wrapper.spotify.model_objects.specification.Track spotifyTrack =
                        (com.wrapper.spotify.model_objects.specification.Track) playlistTrack.getTrack();

                AlbumSimplified album = spotifyTrack.getAlbum();
                ArtistSimplified[] artists = spotifyTrack.getArtists();

                if (spotifyTrack.getPreviewUrl() != null && album.getImages().length > 0) {
                    // 유효한 트랙만 custom epitome.epitome.model.Track 객체로 변환
                    epitome.epitome.model.Track track = new epitome.epitome.model.Track(
                            spotifyTrack.getName(),
                            artists[0].getName(),
                            album.getName(),
                            album.getImages()[0].getUrl(),
                            spotifyTrack.getPreviewUrl(),
                            0.0 // similarity 값은 나중에 설정
                            );

                    validTracks.add(track);
                }
            }

            return validTracks;

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
