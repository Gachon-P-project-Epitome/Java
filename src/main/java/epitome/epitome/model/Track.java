package epitome.epitome.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    private String name;
    private String artistName;
    private String album;
    private String albumImageUrl;
    private String previewUrl;
    private double similarity;
}
