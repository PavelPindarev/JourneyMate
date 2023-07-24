package bg.journey.demo.dto.payload;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PictureUploadPayloadDTO {
    private MultipartFile multipartFile;
    private String title;
}