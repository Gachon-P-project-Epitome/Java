package spotify;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EpitomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpitomeApplication.class, args);
	}

	@Bean
	CommandLineRunner run(SearchTrack searchTrack) {
		return args -> {
			searchTrack.searchTracks_Sync("abcdefu"); // 검색할 트랙 검색 이름
		};
	}

	/*
	@Bean
	CommandLineRunner run(SearchTrackByGenre searchTrackByGenre) {
		return args -> {
			searchTrackByGenre.SearchTrackByGenre_Sync("pop"); // 장르 예시: pop
		};
	}
	 */
	/*
	@Bean
	CommandLineRunner run(SearchTrackFromPlayList searchTrackFromPlayList) {
		return args -> {
			String playlistId = "37i9dQZF1DX3ZeFHRhhi7Y";  // 플레이리스트 ID
			int limit = 10;
			searchTrackFromPlayList.searchPlaylist(playlistId,limit);
		};
	}

	 */
}
