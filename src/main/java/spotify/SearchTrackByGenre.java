package spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Recommendations;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.data.browse.GetRecommendationsRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchTrackByGenre {
    private final CreateToken createToken;

    @Autowired
    public SearchTrackByGenre(CreateToken createToken) {
        this.createToken = createToken;
    }

    public List<TrackInfo> SearchTrackByGenre_Sync(String genre) {
        String accessToken = createToken.accesstoken();
        if (accessToken == null) {
            System.out.println("Failed to get access token.");
            return null;
        }

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        // 장르 기반 추천 트랙 요청
        GetRecommendationsRequest recommendationsRequest = spotifyApi.getRecommendations()
                .limit(20) // 10개의 추천 트랙 가져옴
                .seed_genres(genre) // 장르 시드 설정
                .build();

        List<TrackInfo> trackInfoList = new ArrayList<>();

        try {
            Recommendations recommendations = recommendationsRequest.execute();
            for (TrackSimplified track : recommendations.getTracks()) {
                if (track.getPreviewUrl() != null) {
                    String trackName = track.getName();
                    String artistName = track.getArtists()[0].getName();
                    String previewUrl = track.getPreviewUrl();

                    // TrackInfo 객체에 가수 이름, 음악 이름, 미리 듣기 URL 저장
                    trackInfoList.add(new TrackInfo(artistName, trackName, previewUrl));

                    // 콘솔에 정보 출력
                    System.out.println("가수: " + artistName);
                    System.out.println("음악 이름: " + trackName);
                    System.out.println("미리 듣기 URL: " + previewUrl);
                }
            }

            if (trackInfoList.isEmpty()) {
                System.out.println("No playable track found.");
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return trackInfoList;
    }
}
