package bg.journey.demo.service;

import bg.journey.demo.dto.payload.PictureUploadPayloadDTO;
import bg.journey.demo.exception.CloudinaryException;
import bg.journey.demo.model.entity.PictureEntity;
import bg.journey.demo.repository.PictureRepository;
import com.cloudinary.Cloudinary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
public class CloudinaryService {
    private static final String TEMP_FILE = "temp-file";
    private static final String URL = "url";
    private static final String PUBLIC_ID = "public_id";

    private final Cloudinary cloudinary;
    private final PictureRepository pictureRepository;

    public CloudinaryService(Cloudinary cloudinary, PictureRepository pictureRepository) {
        this.cloudinary = cloudinary;
        this.pictureRepository = pictureRepository;
    }


    public PictureEntity upload(PictureUploadPayloadDTO pictureDTO){
        return upload(pictureDTO, null);
    }

    public PictureEntity upload(PictureUploadPayloadDTO pictureDTO, String folderName) {

        File tempFile = null;
        try {
            tempFile = File.createTempFile(TEMP_FILE, pictureDTO.getMultipartFile().getOriginalFilename());
            pictureDTO.getMultipartFile().transferTo(tempFile);

            Map<String, String> options = null;
            if (folderName != null){
                options = Map.of(
                        "folder", folderName
                );
            }

            @SuppressWarnings("unchecked")
            Map<String, String> uploadResult = cloudinary.
                    uploader().
                    upload(tempFile, options);

            String url = uploadResult.getOrDefault(URL,
                    "https://i.pinimg.com/originals/c5/21/64/c52164749f7460c1ededf8992cd9a6ec.jpg");
            String publicId = uploadResult.getOrDefault(PUBLIC_ID, "");

            return pictureRepository.save(PictureEntity
                    .builder()
                    .publicId(publicId)
                    .url(url)
                    .title(pictureDTO.getTitle())
                    .build());
        } catch (IOException e) {
            throw new CloudinaryException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Cloudinary error: %s", e.getMessage()));
        } finally {
            if (tempFile != null) {
                cleanUp(tempFile.toPath());
            }
        }
    }


    public boolean delete(PictureEntity imageEntity) {
        if (imageEntity != null) {
            try {
                //delete in cloudinary
                this.cloudinary.uploader().destroy(imageEntity.getPublicId(), Map.of());
                //delete in database
                pictureRepository.deleteById(imageEntity.getId());
            } catch (IOException e) {
                throw new CloudinaryException(HttpStatus.INTERNAL_SERVER_ERROR,
                        String.format("Cloudinary error: %s", e.getMessage()));
            }
        }
        return false;
    }

    private void cleanUp(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new CloudinaryException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Cloudinary error: %s", e.getMessage()));
        }
    }
}
