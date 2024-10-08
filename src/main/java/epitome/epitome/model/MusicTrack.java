package epitome.epitome.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MusicTrack {
    private String name;
    private String artistName;
    private String album;
    private String albumImageUrl;
    private String previewUrl;
    private double similarity;
}
