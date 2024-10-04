package epitome.epitome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MakeTrackListService {

    private final FindTrackService findTrackService;
    @Autowired
    public MakeTrackListService(FindTrackService findTrackService) {
        this.findTrackService = findTrackService;
    }



}
