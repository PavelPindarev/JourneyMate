package bg.journey.demo.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PictureDTO {
    private String title;
    private String publicId;
    private String url;
}