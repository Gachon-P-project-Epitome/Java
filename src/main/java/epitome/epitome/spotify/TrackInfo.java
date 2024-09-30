package epitome.epitome.spotify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class TrackInfo {
    private String artistName;
    private String trackName;
    private String previewUrl;
}