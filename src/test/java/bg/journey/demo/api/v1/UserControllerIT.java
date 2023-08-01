package bg.journey.demo.api.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bg.journey.demo.dto.payload.PictureUploadPayloadDTO;
import bg.journey.demo.dto.payload.UpdateUserProfileDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.dto.response.UserProfileDTO;
import bg.journey.demo.model.entity.PictureEntity;
import bg.journey.demo.model.entity.RoleEntity;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.repository.PictureRepository;
import bg.journey.demo.repository.UserRepository;
import bg.journey.demo.security.UserPrincipal;
import bg.journey.demo.service.CloudinaryService;
import bg.journey.demo.service.UserService;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

public class UserControllerIT {
    /**
     * Method under test: {@link UserController#getMyProfile(UserPrincipal)}
     */
    @Test
    public void testGetMyProfile() {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(new UserEntity()));
        ModelMapper mapper = new ModelMapper();
        UserController userController = new UserController(new UserService(userRepository, mapper,
                new CloudinaryService(new Cloudinary(), mock(PictureRepository.class))));
        ResponseEntity<ResponseDTO<UserProfileDTO>> actualMyProfile = userController.getMyProfile(new UserPrincipal());
        assertTrue(actualMyProfile.hasBody());
        assertTrue(actualMyProfile.getHeaders().isEmpty());
        assertEquals(200, actualMyProfile.getStatusCodeValue());
        ResponseDTO<UserProfileDTO> body = actualMyProfile.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get your profile info!", body.getMessage());
        UserProfileDTO content = body.getContent();
        assertNull(content.getBirthdate());
        assertNull(content.getUsername());
        assertNull(content.getLastName());
        assertNull(content.getFirstName());
        assertNull(content.getEmail());
        assertNull(content.getCreatedOn());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserController#getMyProfile(UserPrincipal)}
     */
    @Test
    public void testGetMyProfile2() {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(new UserEntity()));
        ModelMapper mapper = mock(ModelMapper.class);
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        when(mapper.map(Mockito.<Object>any(), Mockito.<Class<UserProfileDTO>>any())).thenReturn(userProfileDTO);
        UserController userController = new UserController(new UserService(userRepository, mapper,
                new CloudinaryService(new Cloudinary(), mock(PictureRepository.class))));
        ResponseEntity<ResponseDTO<UserProfileDTO>> actualMyProfile = userController.getMyProfile(new UserPrincipal());
        assertTrue(actualMyProfile.hasBody());
        assertTrue(actualMyProfile.getHeaders().isEmpty());
        assertEquals(200, actualMyProfile.getStatusCodeValue());
        ResponseDTO<UserProfileDTO> body = actualMyProfile.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get your profile info!", body.getMessage());
        assertSame(userProfileDTO, body.getContent());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
        verify(mapper).map(Mockito.<Object>any(), Mockito.<Class<UserProfileDTO>>any());
    }

    /**
     * Method under test: {@link UserController#getMyProfile(UserPrincipal)}
     */
    @Test
    public void testGetMyProfile3() {

        UserService userService = mock(UserService.class);
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        when(userService.getMyProfile(Mockito.<UserPrincipal>any())).thenReturn(userProfileDTO);
        UserController userController = new UserController(userService);
        ResponseEntity<ResponseDTO<UserProfileDTO>> actualMyProfile = userController.getMyProfile(new UserPrincipal());
        assertTrue(actualMyProfile.hasBody());
        assertTrue(actualMyProfile.getHeaders().isEmpty());
        assertEquals(200, actualMyProfile.getStatusCodeValue());
        ResponseDTO<UserProfileDTO> body = actualMyProfile.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get your profile info!", body.getMessage());
        assertSame(userProfileDTO, body.getContent());
        verify(userService).getMyProfile(Mockito.<UserPrincipal>any());
    }

    /**
     * Method under test: {@link UserController#getUserProfile(Long)}
     */
    @Test
    public void testGetUserProfile() {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(new UserEntity()));
        ModelMapper mapper = new ModelMapper();
        ResponseEntity<ResponseDTO<UserProfileDTO>> actualUserProfile = (new UserController(new UserService(
                userRepository, mapper, new CloudinaryService(new Cloudinary(), mock(PictureRepository.class)))))
                .getUserProfile(1L);
        assertTrue(actualUserProfile.hasBody());
        assertTrue(actualUserProfile.getHeaders().isEmpty());
        assertEquals(200, actualUserProfile.getStatusCodeValue());
        ResponseDTO<UserProfileDTO> body = actualUserProfile.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get user's profile info!", body.getMessage());
        UserProfileDTO content = body.getContent();
        assertNull(content.getBirthdate());
        assertNull(content.getUsername());
        assertNull(content.getLastName());
        assertNull(content.getFirstName());
        assertNull(content.getEmail());
        assertNull(content.getCreatedOn());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserController#getUserProfile(Long)}
     */
    @Test
    public void testGetUserProfile2() {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(new UserEntity()));
        ModelMapper mapper = mock(ModelMapper.class);
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        when(mapper.map(Mockito.<Object>any(), Mockito.<Class<UserProfileDTO>>any())).thenReturn(userProfileDTO);
        ResponseEntity<ResponseDTO<UserProfileDTO>> actualUserProfile = (new UserController(new UserService(
                userRepository, mapper, new CloudinaryService(new Cloudinary(), mock(PictureRepository.class)))))
                .getUserProfile(1L);
        assertTrue(actualUserProfile.hasBody());
        assertTrue(actualUserProfile.getHeaders().isEmpty());
        assertEquals(200, actualUserProfile.getStatusCodeValue());
        ResponseDTO<UserProfileDTO> body = actualUserProfile.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get user's profile info!", body.getMessage());
        assertSame(userProfileDTO, body.getContent());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(mapper).map(Mockito.<Object>any(), Mockito.<Class<UserProfileDTO>>any());
    }

    /**
     * Method under test: {@link UserController#getUserProfile(Long)}
     */
    @Test
    public void testGetUserProfile3() {

        UserService userService = mock(UserService.class);
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        when(userService.getUserProfile(Mockito.<Long>any())).thenReturn(userProfileDTO);
        ResponseEntity<ResponseDTO<UserProfileDTO>> actualUserProfile = (new UserController(userService))
                .getUserProfile(1L);
        assertTrue(actualUserProfile.hasBody());
        assertTrue(actualUserProfile.getHeaders().isEmpty());
        assertEquals(200, actualUserProfile.getStatusCodeValue());
        ResponseDTO<UserProfileDTO> body = actualUserProfile.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get user's profile info!", body.getMessage());
        assertSame(userProfileDTO, body.getContent());
        verify(userService).getUserProfile(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserController#updateMyProfile(UserPrincipal, UpdateUserProfileDTO, BindingResult)}
     */
    @Test
    public void testUpdateMyProfile() {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.<UserEntity>any())).thenReturn(new UserEntity());
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(new UserEntity()));
        ModelMapper mapper = new ModelMapper();
        UserController userController = new UserController(new UserService(userRepository, mapper,
                new CloudinaryService(new Cloudinary(), mock(PictureRepository.class))));
        UserPrincipal userPrincipal = new UserPrincipal();
        UpdateUserProfileDTO updateUserProfileDTO = new UpdateUserProfileDTO();
        ResponseEntity<ResponseDTO<Object>> actualUpdateMyProfileResult = userController.updateMyProfile(userPrincipal,
                updateUserProfileDTO, new BindException("Target", "Object Name"));
        assertTrue(actualUpdateMyProfileResult.hasBody());
        assertTrue(actualUpdateMyProfileResult.getHeaders().isEmpty());
        assertEquals(200, actualUpdateMyProfileResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualUpdateMyProfileResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Profile updated successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).save(Mockito.<UserEntity>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserController#updateMyProfile(UserPrincipal, UpdateUserProfileDTO, BindingResult)}
     */
    @Test
    public void testUpdateMyProfile2() {

        UserService userService = mock(UserService.class);
        doNothing().when(userService).updateMyProfile(Mockito.<UserPrincipal>any(), Mockito.<UpdateUserProfileDTO>any());
        UserController userController = new UserController(userService);
        UserPrincipal userPrincipal = new UserPrincipal();
        UpdateUserProfileDTO updateUserProfileDTO = new UpdateUserProfileDTO();
        ResponseEntity<ResponseDTO<Object>> actualUpdateMyProfileResult = userController.updateMyProfile(userPrincipal,
                updateUserProfileDTO, new BindException("Target", "Object Name"));
        assertTrue(actualUpdateMyProfileResult.hasBody());
        assertTrue(actualUpdateMyProfileResult.getHeaders().isEmpty());
        assertEquals(200, actualUpdateMyProfileResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualUpdateMyProfileResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Profile updated successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userService).updateMyProfile(Mockito.<UserPrincipal>any(), Mockito.<UpdateUserProfileDTO>any());
    }

    /**
     * Method under test: {@link UserController#updateMyProfile(UserPrincipal, UpdateUserProfileDTO, BindingResult)}
     */
    @Test
    public void testUpdateMyProfile3() {

        UserController userController = new UserController(mock(UserService.class));
        UserPrincipal userPrincipal = new UserPrincipal();
        UpdateUserProfileDTO updateUserProfileDTO = new UpdateUserProfileDTO();
        BeanPropertyBindingResult bindingResult = mock(BeanPropertyBindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());
        when(bindingResult.hasErrors()).thenReturn(true);
        ResponseEntity<ResponseDTO<Object>> actualUpdateMyProfileResult = userController.updateMyProfile(userPrincipal,
                updateUserProfileDTO, bindingResult);
        assertTrue(actualUpdateMyProfileResult.hasBody());
        assertTrue(actualUpdateMyProfileResult.getHeaders().isEmpty());
        assertEquals(400, actualUpdateMyProfileResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualUpdateMyProfileResult.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("", body.getMessage());
        assertNull(body.getContent());
        verify(bindingResult).hasErrors();
        verify(bindingResult).getAllErrors();
    }

    /**
     * Method under test: {@link UserController#setUserProfilePicture(UserPrincipal, String, MultipartFile)}
     */
    @Test
    public void testSetUserProfilePicture() throws IOException {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(new UserEntity()));
        Uploader uploader = mock(Uploader.class);
        when(uploader.upload(Mockito.<Object>any(), Mockito.<Map<Object, Object>>any())).thenReturn(new HashMap<>());
        Cloudinary cloudinary = mock(Cloudinary.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        PictureRepository pictureRepository = mock(PictureRepository.class);
        when(pictureRepository.save(Mockito.<PictureEntity>any()))
                .thenReturn(new PictureEntity("Dr", "42", "https://example.org/example"));
        CloudinaryService cloudinaryService = new CloudinaryService(cloudinary, pictureRepository);

        UserController userController = new UserController(
                new UserService(userRepository, new ModelMapper(), cloudinaryService));
        UserPrincipal userPrincipal = new UserPrincipal();
        ResponseEntity<ResponseDTO<Object>> actualSetUserProfilePictureResult = userController.setUserProfilePicture(
                userPrincipal, "Dr", new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        assertTrue(actualSetUserProfilePictureResult.hasBody());
        assertTrue(actualSetUserProfilePictureResult.getHeaders().isEmpty());
        assertEquals(200, actualSetUserProfilePictureResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualSetUserProfilePictureResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Profile Picture set successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
        verify(cloudinary).uploader();
        verify(uploader).upload(Mockito.<Object>any(), Mockito.<Map<Object, Object>>any());
        verify(pictureRepository).save(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link UserController#setUserProfilePicture(UserPrincipal, String, MultipartFile)}
     */
    @Test
    public void testSetUserProfilePicture2() throws IOException {

        UserRepository userRepository = mock(UserRepository.class);
        LocalDate birthdate = LocalDate.of(1970, 1, 1);
        HashSet<RoleEntity> roles = new HashSet<>();
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(new UserEntity("janedoe", "jane.doe@example.org", "Jane", "Doe", "iloveyou",
                        birthdate, roles, new PictureEntity("Dr", "42", "https://example.org/example"), true, true, true, true)));
        Uploader uploader = mock(Uploader.class);
        when(uploader.destroy(Mockito.<String>any(), Mockito.<Map<Object, Object>>any())).thenReturn(new HashMap<>());
        when(uploader.upload(Mockito.<Object>any(), Mockito.<Map<Object, Object>>any())).thenReturn(new HashMap<>());
        Cloudinary cloudinary = mock(Cloudinary.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        PictureRepository pictureRepository = mock(PictureRepository.class);
        doNothing().when(pictureRepository).deleteById(Mockito.<Long>any());
        when(pictureRepository.save(Mockito.<PictureEntity>any()))
                .thenReturn(new PictureEntity("Dr", "42", "https://example.org/example"));
        CloudinaryService cloudinaryService = new CloudinaryService(cloudinary, pictureRepository);

        UserController userController = new UserController(
                new UserService(userRepository, new ModelMapper(), cloudinaryService));
        UserPrincipal userPrincipal = new UserPrincipal();
        ResponseEntity<ResponseDTO<Object>> actualSetUserProfilePictureResult = userController.setUserProfilePicture(
                userPrincipal, "Dr", new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        assertTrue(actualSetUserProfilePictureResult.hasBody());
        assertTrue(actualSetUserProfilePictureResult.getHeaders().isEmpty());
        assertEquals(200, actualSetUserProfilePictureResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualSetUserProfilePictureResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Profile Picture set successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
        verify(cloudinary, atLeast(1)).uploader();
        verify(uploader).destroy(Mockito.<String>any(), Mockito.<Map<Object, Object>>any());
        verify(uploader).upload(Mockito.<Object>any(), Mockito.<Map<Object, Object>>any());
        verify(pictureRepository).save(Mockito.<PictureEntity>any());
        verify(pictureRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserController#setUserProfilePicture(UserPrincipal, String, MultipartFile)}
     */
    @Test
    public void testSetUserProfilePicture3() throws IOException {

        UserRepository userRepository = mock(UserRepository.class);
        LocalDate birthdate = LocalDate.of(1970, 1, 1);
        HashSet<RoleEntity> roles = new HashSet<>();
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(new UserEntity("janedoe", "jane.doe@example.org", "Jane", "Doe", "iloveyou",
                        birthdate, roles, new PictureEntity("Dr", "42", "https://example.org/example"), true, true, true, true)));
        Uploader uploader = mock(Uploader.class);
        when(uploader.upload(Mockito.<Object>any(), Mockito.<Map<Object, Object>>any())).thenReturn(new HashMap<>());
        Cloudinary cloudinary = mock(Cloudinary.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        PictureRepository pictureRepository = mock(PictureRepository.class);
        when(pictureRepository.save(Mockito.<PictureEntity>any())).thenReturn(null);
        CloudinaryService cloudinaryService = new CloudinaryService(cloudinary, pictureRepository);

        UserController userController = new UserController(
                new UserService(userRepository, new ModelMapper(), cloudinaryService));
        UserPrincipal userPrincipal = new UserPrincipal();
        ResponseEntity<ResponseDTO<Object>> actualSetUserProfilePictureResult = userController.setUserProfilePicture(
                userPrincipal, "Dr", new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        assertTrue(actualSetUserProfilePictureResult.hasBody());
        assertTrue(actualSetUserProfilePictureResult.getHeaders().isEmpty());
        assertEquals(200, actualSetUserProfilePictureResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualSetUserProfilePictureResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Profile Picture set successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
        verify(cloudinary).uploader();
        verify(uploader).upload(Mockito.<Object>any(), Mockito.<Map<Object, Object>>any());
        verify(pictureRepository).save(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link UserController#setUserProfilePicture(UserPrincipal, String, MultipartFile)}
     */
    @Test
    public void testSetUserProfilePicture4() throws IOException {

        UserRepository userRepository = mock(UserRepository.class);
        LocalDate birthdate = LocalDate.of(1970, 1, 1);
        HashSet<RoleEntity> roles = new HashSet<>();
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(new UserEntity("janedoe", "jane.doe@example.org", "Jane", "Doe", "iloveyou",
                        birthdate, roles, new PictureEntity("Dr", "42", "https://example.org/example"), true, true, true, true)));
        CloudinaryService cloudinaryService = mock(CloudinaryService.class);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenReturn(true);
        when(cloudinaryService.upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any()))
                .thenReturn(new PictureEntity("Dr", "42", "https://example.org/example"));
        UserController userController = new UserController(
                new UserService(userRepository, new ModelMapper(), cloudinaryService));
        UserPrincipal userPrincipal = new UserPrincipal();
        ResponseEntity<ResponseDTO<Object>> actualSetUserProfilePictureResult = userController.setUserProfilePicture(
                userPrincipal, "Dr", new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        assertTrue(actualSetUserProfilePictureResult.hasBody());
        assertTrue(actualSetUserProfilePictureResult.getHeaders().isEmpty());
        assertEquals(200, actualSetUserProfilePictureResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualSetUserProfilePictureResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Profile Picture set successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
        verify(cloudinaryService).upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link UserController#setUserProfilePicture(UserPrincipal, String, MultipartFile)}
     */
    @Test
    public void testSetUserProfilePicture5() throws IOException {

        UserService userService = mock(UserService.class);
        doNothing().when(userService)
                .setProfilePicture(Mockito.<UserPrincipal>any(), Mockito.<PictureUploadPayloadDTO>any());
        UserController userController = new UserController(userService);
        UserPrincipal userPrincipal = new UserPrincipal();
        ResponseEntity<ResponseDTO<Object>> actualSetUserProfilePictureResult = userController.setUserProfilePicture(
                userPrincipal, "Dr", new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        assertTrue(actualSetUserProfilePictureResult.hasBody());
        assertTrue(actualSetUserProfilePictureResult.getHeaders().isEmpty());
        assertEquals(200, actualSetUserProfilePictureResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualSetUserProfilePictureResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Profile Picture set successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userService).setProfilePicture(Mockito.<UserPrincipal>any(), Mockito.<PictureUploadPayloadDTO>any());
    }

    /**
     * Method under test: {@link UserController#deleteUserProfilePicture(UserPrincipal)}
     */
    @Test
    public void testDeleteUserProfilePicture() {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.<UserEntity>any())).thenReturn(new UserEntity());
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(new UserEntity()));
        ModelMapper mapper = new ModelMapper();
        UserController userController = new UserController(new UserService(userRepository, mapper,
                new CloudinaryService(new Cloudinary(), mock(PictureRepository.class))));
        ResponseEntity<ResponseDTO<Object>> actualDeleteUserProfilePictureResult = userController
                .deleteUserProfilePicture(new UserPrincipal());
        assertTrue(actualDeleteUserProfilePictureResult.hasBody());
        assertTrue(actualDeleteUserProfilePictureResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteUserProfilePictureResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteUserProfilePictureResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Profile Picture deleted successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).save(Mockito.<UserEntity>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserController#deleteUserProfilePicture(UserPrincipal)}
     */
    @Test
    public void testDeleteUserProfilePicture2() {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.<UserEntity>any())).thenReturn(new UserEntity());
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(new UserEntity()));
        CloudinaryService cloudinaryService = mock(CloudinaryService.class);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenReturn(true);
        UserController userController = new UserController(
                new UserService(userRepository, new ModelMapper(), cloudinaryService));
        ResponseEntity<ResponseDTO<Object>> actualDeleteUserProfilePictureResult = userController
                .deleteUserProfilePicture(new UserPrincipal());
        assertTrue(actualDeleteUserProfilePictureResult.hasBody());
        assertTrue(actualDeleteUserProfilePictureResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteUserProfilePictureResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteUserProfilePictureResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Profile Picture deleted successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).save(Mockito.<UserEntity>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link UserController#deleteUserProfilePicture(UserPrincipal)}
     */
    @Test
    public void testDeleteUserProfilePicture3() {

        UserService userService = mock(UserService.class);
        doNothing().when(userService).deleteProfileImage(Mockito.<UserPrincipal>any());
        UserController userController = new UserController(userService);
        ResponseEntity<ResponseDTO<Object>> actualDeleteUserProfilePictureResult = userController
                .deleteUserProfilePicture(new UserPrincipal());
        assertTrue(actualDeleteUserProfilePictureResult.hasBody());
        assertTrue(actualDeleteUserProfilePictureResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteUserProfilePictureResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteUserProfilePictureResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Profile Picture deleted successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userService).deleteProfileImage(Mockito.<UserPrincipal>any());
    }

    /**
     * Method under test: {@link UserController#banUserFromApp(Long, UserPrincipal)}
     */
    @Test
    public void testBanUserFromApp() {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(new UserEntity()));
        ModelMapper mapper = new ModelMapper();
        UserController userController = new UserController(new UserService(userRepository, mapper,
                new CloudinaryService(new Cloudinary(), mock(PictureRepository.class))));
        ResponseEntity<ResponseDTO<Object>> actualBanUserFromAppResult = userController.banUserFromApp(1L,
                new UserPrincipal());
        assertTrue(actualBanUserFromAppResult.hasBody());
        assertTrue(actualBanUserFromAppResult.getHeaders().isEmpty());
        assertEquals(200, actualBanUserFromAppResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualBanUserFromAppResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("User banned from the app successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserController#banUserFromApp(Long, UserPrincipal)}
     */
    @Test
    public void testBanUserFromApp2() {

        UserService userService = mock(UserService.class);
        doNothing().when(userService).banUserFromApp(Mockito.<UserPrincipal>any(), Mockito.<Long>any());
        UserController userController = new UserController(userService);
        ResponseEntity<ResponseDTO<Object>> actualBanUserFromAppResult = userController.banUserFromApp(1L,
                new UserPrincipal());
        assertTrue(actualBanUserFromAppResult.hasBody());
        assertTrue(actualBanUserFromAppResult.getHeaders().isEmpty());
        assertEquals(200, actualBanUserFromAppResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualBanUserFromAppResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("User banned from the app successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userService).banUserFromApp(Mockito.<UserPrincipal>any(), Mockito.<Long>any());
    }
}

