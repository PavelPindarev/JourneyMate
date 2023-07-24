package bg.journey.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloudName}")
    private String cloudName;
    @Value("${cloudinary.apiKey}")
    private String apiKey;
    @Value("${cloudinary.apiSecret}")
    private String apiSecret;
}