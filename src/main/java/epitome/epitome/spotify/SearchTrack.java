package epitome.epitome.spotify;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchTrack {
    private final CreateToken createToken;
    private final DownloadUtils downloadUtils;

    @Autowired
    public SearchTrack(CreateToken createToken, DownloadUtils downloadUtils) {
        this.createToken = createToken;
        this.downloadUtils = downloadUtils;
    }

    public ArtistSimplified searchTracks_Sync(String q) {
        String accessToken = createToken.accesstoken();
        if (accessToken == null) {
            System.out.println("Failed to get access token.");
            return null;
        }

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(q)
                .market(CountryCode.US)
                .limit(10)
                .build();

        Track track = null;
        ArtistSimplified artist = null;
        String albumImageUrl = null; // 앨범 이미지 URL을 저장할 변수

        try {
            final Paging<Track> trackPaging = searchTracksRequest.execute();
            for (Track t : trackPaging.getItems()) {
                if (t.getPreviewUrl() != null) {
                    track = t; // 첫 번째 미리듣기 가능한 트랙
                    System.out.println("제목 : " + track.getName());
                    System.out.println("가수 : " + track.getArtists()[0].getName());
                    System.out.println("미리 듣기 url : " + track.getPreviewUrl());
                    artist = track.getArtists()[0]; // 해당 노래를 부르는 메인 가수

                    // 앨범 이미지 가져오기
                    Image[] albumImages = track.getAlbum().getImages();
                    if (albumImages.length > 0) {
                        albumImageUrl = albumImages[0].getUrl(); // 가장 큰 이미지를 선택
                        System.out.println("앨범 이미지 URL: " + albumImageUrl);
                    }

                    // 미리듣기 파일을 다운로드
                    downloadUtils.downloadPreviewAsMp3(track.getPreviewUrl(), track.getName() + ".mp3");
                    break; // 첫 번째 미리듣기 가능한 트랙을 찾으면 루프 종료
                }
            }
            if (track == null) {
                System.out.println("No playable track found.");
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return artist;
    }
}
