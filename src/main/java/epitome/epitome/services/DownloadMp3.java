package epitome.epitome.services;

import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DownloadMp3 {
    public void downloadPreviewAsMp3(String previewUrl, String fileName) {
        try (InputStream inputStream = new URL(previewUrl).openStream();
             FileOutputStream outputStream = new FileOutputStream(fileName)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("MP3 파일 다운로드 완료: " + fileName);
        } catch (IOException e) {
            System.out.println("MP3 파일 다운로드 중 오류 발생: " + e.getMessage());
        }
    }

    public void downloadMultipleTracksAsMp3(Map<String, String> previewUrlsWithId) {
        // 스레드 풀 생성 (예: 10개의 스레드를 사용하여 병렬 처리)
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        previewUrlsWithId.forEach((previewUrl, id) -> {
            String fileName ="/Users/habeomsu/musicfolder/"+ id + ".mp3";

            // 각 URL에 대해 다운로드 작업 제출
            executorService.submit(() -> downloadPreviewAsMp3(previewUrl, fileName));
        });

        // 모든 작업이 완료될 때까지 기다린 후 스레드 풀 종료
        executorService.shutdown();
    }
}

