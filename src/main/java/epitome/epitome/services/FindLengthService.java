package epitome.epitome.services;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import epitome.epitome.model.MusicTrack;
import epitome.epitome.spotify.CreateToken;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FindLengthService {
    private final CreateToken createToken;

    @Autowired
    public FindLengthService(CreateToken createToken) {
        this.createToken = createToken;
    }

    public List<MusicTrack> searchLength(String playlistId) {
        String accessToken = createToken.accesstoken();
        if (accessToken == null) {
            System.out.println("Failed to get access token.");
            return Collections.emptyList();
        }

        // Spotify API 설정
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        int offset = 0;
        int limit = 100; // 최대 100개의 트랙 요청
        List<MusicTrack> validTracks = new ArrayList<>();

        try {
            while (true) {
                // 페이지네이션을 통해 트랙을 가져오기
                GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi
                        .getPlaylistsItems(playlistId)
                        .offset(offset)
                        .limit(limit)
                        .build();

                Paging<PlaylistTrack> playlistTracks = getPlaylistsItemsRequest.execute();

                if (playlistTracks.getItems().length == 0) {
                    break; // 더 이상 가져올 트랙이 없으면 루프 종료
                }

                // 현재 페이지의 트랙 추가
                for (PlaylistTrack playlistTrack : playlistTracks.getItems()) {
                    com.wrapper.spotify.model_objects.specification.Track spotifyTrack =
                            (com.wrapper.spotify.model_objects.specification.Track) playlistTrack.getTrack();

                    AlbumSimplified album = spotifyTrack.getAlbum();
                    ArtistSimplified[] artists = spotifyTrack.getArtists();

                    // 유효한 트랙만 custom epitome.epitome.model.Track 객체로 변환
                    MusicTrack musicTrack = new MusicTrack(
                            spotifyTrack.getName(),
                            artists[0].getName(),
                            album.getName(),
                            album.getImages().length > 0 ? album.getImages()[0].getUrl() : null, // 이미지가 있을 경우에만 URL 설정
                            spotifyTrack.getPreviewUrl(),
                            0.0, // similarity 값은 나중에 설정
                            spotifyTrack.getId()
                    );

                    validTracks.add(musicTrack);
                }

                // 다음 페이지로 이동
                offset += limit;
            }

            System.out.println("전체 트랙 수: " + validTracks.size());
            return validTracks;

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
