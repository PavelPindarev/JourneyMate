package bg.journey.demo.service;

import bg.journey.demo.dto.payload.PictureUploadPayloadDTO;
import bg.journey.demo.dto.payload.UpdateUserProfileDTO;
import bg.journey.demo.dto.response.UserProfileDTO;
import bg.journey.demo.exception.NotAuthorizedException;
import bg.journey.demo.exception.ResourceNotFoundException;
import bg.journey.demo.model.entity.PictureEntity;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.repository.UserRepository;
import bg.journey.demo.security.UserPrincipal;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static bg.journey.demo.config.AppConstants.DEFAULT_USER_PICTURE_CLOUDINARY_FOLDER;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final CloudinaryService cloudinaryService;

    public UserService(UserRepository userRepository, ModelMapper mapper, CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.cloudinaryService = cloudinaryService;
    }


    public UserProfileDTO getMyProfile(UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new NotAuthorizedException();
        }
        UserEntity userEntity = userRepository
                .findByUsernameOrEmail(userPrincipal.getUsername(), userPrincipal.getUsername())
                .orElseThrow(NotAuthorizedException::new);

        return mapper.map(userEntity, UserProfileDTO.class);
    }

    public UserProfileDTO getUserProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(ResourceNotFoundException::new);

        return mapper.map(userEntity, UserProfileDTO.class);
    }

    @Transactional
    public void updateMyProfile(UserPrincipal userPrincipal, UpdateUserProfileDTO updateUserProfileDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        userEntity.setUsername(updateUserProfileDTO.getUsername());
        userEntity.setBirthdate(updateUserProfileDTO.getBirthdate());
        userEntity.setFirstName(updateUserProfileDTO.getFirstName());
        userEntity.setLastName(updateUserProfileDTO.getLastName());
        userRepository.save(userEntity);
    }

    @Transactional
    public void setProfilePicture(UserPrincipal userPrincipal, PictureUploadPayloadDTO pictureDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        //first upload the new image
        PictureEntity pictureEntity = cloudinaryService.upload(pictureDTO,
                DEFAULT_USER_PICTURE_CLOUDINARY_FOLDER);

        //if the new image is uploaded
        if (pictureEntity != null && userEntity.getProfilePicture() != null) {
            //delete the old image
            cloudinaryService.delete(userEntity.getProfilePicture());
        }

        //set the new profile image
        userEntity.setProfilePicture(pictureEntity);
    }

    @Transactional
    public void deleteProfileImage(UserPrincipal userPrincipal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);


        PictureEntity profilePicture = userEntity.getProfilePicture();
        userEntity.setProfilePicture(null);
        this.userRepository.save(userEntity);

        cloudinaryService.delete(profilePicture);
    }

    @Transactional
    public void banUserFromApp(UserPrincipal principal, Long userId) {
        UserEntity userForBan = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "User not found!"));
        userForBan.setEnabled(false);
    }

    public boolean isOwner(String userName, Long id) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userName,
                userName).orElseThrow(NotAuthorizedException::new);

        return userEntity.getId().equals(id);
    }
}
