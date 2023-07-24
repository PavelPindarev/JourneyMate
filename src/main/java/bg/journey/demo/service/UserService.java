package bg.journey.demo.service;

import bg.journey.demo.dto.payload.UpdateUserProfileDTO;
import bg.journey.demo.dto.response.UserProfileDTO;
import bg.journey.demo.exception.NotAuthorizedException;
import bg.journey.demo.exception.ResourceNotFoundException;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.repository.UserRepository;
import bg.journey.demo.security.UserPrincipal;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserService(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
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

    // UserProfileDTO getUserProfile(Long userId);
    //
    //    MyProfileDTO getMyProfile(UserPrincipal userPrincipal);
    //void setProfileImage(UserPrincipal userPrincipal, ImageUploadPayloadDTO imageUploadDTO);
    // void deleteProfileImage(UserPrincipal userPrincipal);
    //
    //    void updateMyProfile(UserPrincipal userPrincipal, UpdateUserProfileDTO updateUserProfileDTO);

//    void banUserFromApp(UserPrincipal principal, Long userId);
//    void deleteMyProfile(UserPrincipal principal);
}
