package epitome.epitome.spotify;

import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component
public class DownloadUtils {

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
}
