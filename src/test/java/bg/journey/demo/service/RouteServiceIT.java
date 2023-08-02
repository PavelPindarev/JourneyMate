package bg.journey.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bg.journey.demo.dto.payload.PictureUploadPayloadDTO;
import bg.journey.demo.dto.payload.RouteCreateDTO;
import bg.journey.demo.dto.response.RouteDetailsViewDTO;
import bg.journey.demo.exception.CategoryNotFoundException;
import bg.journey.demo.model.entity.CategoryEntity;
import bg.journey.demo.model.entity.PictureEntity;
import bg.journey.demo.model.entity.ReactionEntity;
import bg.journey.demo.model.entity.RouteEntity;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.model.enums.LevelEnum;
import bg.journey.demo.model.mapper.CategoryMapper;
import bg.journey.demo.model.mapper.PictureMapper;
import bg.journey.demo.model.mapper.ReactionMapper;
import bg.journey.demo.repository.CategoryRepository;
import bg.journey.demo.repository.ReactionRepository;
import bg.journey.demo.repository.RouteRepository;
import bg.journey.demo.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@ActiveProfiles({"development"})
@RunWith(SpringJUnit4ClassRunner.class)
public class RouteServiceIT {
    @MockBean
    private CategoryMapper categoryMapper;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private CloudinaryService cloudinaryService;

    @MockBean
    private PictureMapper pictureMapper;

    @MockBean
    private ReactionMapper reactionMapper;

    @MockBean
    private ReactionRepository reactionRepository;

    @MockBean
    private RouteRepository routeRepository;

    @Autowired
    private RouteService routeService;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link RouteService#getAllRoutes()}
     */
    @Test
    public void testGetAllRoutes() {
        assertEquals(1, routeService.getAllRoutes().size());
    }

    /**
     * Method under test: {@link RouteService#getRouteById(Long)}
     */
    @Test
    public void testGetRouteById() {
        RouteDetailsViewDTO actualRouteById = routeService.getRouteById(1L);
        assertEquals("user4o", actualRouteById.getAuthorUsername());
        assertNull(actualRouteById.getVideoUrl());
        assertTrue(actualRouteById.getReactions().isEmpty());
        assertTrue(actualRouteById.getPictures().isEmpty());
        assertEquals("Връх Кумата", actualRouteById.getName());
        assertNull(actualRouteById.getMainPicture());
        assertEquals(LevelEnum.BEGINNER, actualRouteById.getLevelType());
        assertEquals(1L, actualRouteById.getId().longValue());
        assertEquals(
                "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"J!Track Gallery - http://jtrackgallery.net/forum\""
                + " version=\"1.1\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1"
                + "/1/gpx.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >",
                actualRouteById.getGpxCoordinates());
        assertEquals(
                "Един от най-удобните изходни пунктове за изкачване на Черни връх (2290 м) във Витоша е хижа Кумата.",
                actualRouteById.getDescription());
        assertEquals(2, actualRouteById.getCategories().size());
    }

    /**
     * Method under test: {@link RouteService#createRoute(RouteCreateDTO, String)}
     */
    @Test
    public void testCreateRoute() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        when(routeRepository.save(Mockito.<RouteEntity>any())).thenReturn(routeEntity);

        PictureEntity profilePicture2 = new PictureEntity();
        profilePicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setId(1L);
        profilePicture2.setPublicId("42");
        profilePicture2.setTitle("Dr");
        profilePicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setUrl("https://example.org/example");

        UserEntity userEntity = new UserEntity();
        userEntity.setAccountExpired(true);
        userEntity.setBirthdate(LocalDate.of(1970, 1, 1));
        userEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setCredentialsExpired(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(1L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setProfilePicture(profilePicture2);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any())).thenReturn(ofResult);

        RouteCreateDTO routeCreateDTO = new RouteCreateDTO();
        routeCreateDTO.setCategoriesId(new HashSet<>());
        routeService.createRoute(routeCreateDTO, "Principal Name");
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link RouteService#getAllCategories()}
     */
    @Test
    public void testGetAllCategories() {
        assertEquals(3, routeService.getAllCategories().size());
    }

    /**
     * Method under test: {@link RouteService#setMainPicture(Long, PictureUploadPayloadDTO)}
     */
    @Test
    public void testSetMainPicture() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenReturn(true);
        when(cloudinaryService.upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any()))
                .thenReturn(pictureEntity);
        routeService.setMainPicture(1L, new PictureUploadPayloadDTO());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(cloudinaryService).upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteService#setMainPicture(Long, PictureUploadPayloadDTO)}
     */
    @Test
    public void testSetMainPicture2() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenThrow(new CategoryNotFoundException());
        when(cloudinaryService.upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any()))
                .thenReturn(pictureEntity);
        assertThrows(CategoryNotFoundException.class,
                () -> routeService.setMainPicture(1L, new PictureUploadPayloadDTO()));
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(cloudinaryService).upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteService#setMainPicture(Long, PictureUploadPayloadDTO)}
     */
    @Test
    public void testSetMainPicture3() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getMainPicture()).thenReturn(pictureEntity);
        doNothing().when(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setId(Mockito.<Long>any());
        doNothing().when(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setAuthor(Mockito.<UserEntity>any());
        doNothing().when(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        doNothing().when(routeEntity).setDescription(Mockito.<String>any());
        doNothing().when(routeEntity).setGpxCoordinates(Mockito.<String>any());
        doNothing().when(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        doNothing().when(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        doNothing().when(routeEntity).setName(Mockito.<String>any());
        doNothing().when(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        doNothing().when(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        doNothing().when(routeEntity).setVideoUrl(Mockito.<String>any());
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        PictureEntity pictureEntity2 = new PictureEntity();
        pictureEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity2.setId(1L);
        pictureEntity2.setPublicId("42");
        pictureEntity2.setTitle("Dr");
        pictureEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity2.setUrl("https://example.org/example");
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenReturn(true);
        when(cloudinaryService.upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any()))
                .thenReturn(pictureEntity2);
        routeService.setMainPicture(1L, new PictureUploadPayloadDTO());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity, atLeast(1)).getMainPicture();
        verify(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setId(Mockito.<Long>any());
        verify(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setAuthor(Mockito.<UserEntity>any());
        verify(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        verify(routeEntity).setDescription(Mockito.<String>any());
        verify(routeEntity).setGpxCoordinates(Mockito.<String>any());
        verify(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        verify(routeEntity, atLeast(1)).setMainPicture(Mockito.<PictureEntity>any());
        verify(routeEntity).setName(Mockito.<String>any());
        verify(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        verify(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        verify(routeEntity).setVideoUrl(Mockito.<String>any());
        verify(cloudinaryService).upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteService#addPicture(Long, PictureUploadPayloadDTO)}
     */
    @Test
    public void testAddPicture() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");
        when(cloudinaryService.upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any()))
                .thenReturn(pictureEntity);
        routeService.addPicture(1L, new PictureUploadPayloadDTO());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(cloudinaryService).upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link RouteService#addPicture(Long, PictureUploadPayloadDTO)}
     */
    @Test
    public void testAddPicture2() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(cloudinaryService.upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any()))
                .thenThrow(new CategoryNotFoundException());
        assertThrows(CategoryNotFoundException.class, () -> routeService.addPicture(1L, new PictureUploadPayloadDTO()));
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(cloudinaryService).upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link RouteService#addPicture(Long, PictureUploadPayloadDTO)}
     */
    @Test
    public void testAddPicture3() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getPictures()).thenReturn(new HashSet<>());
        doNothing().when(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setId(Mockito.<Long>any());
        doNothing().when(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setAuthor(Mockito.<UserEntity>any());
        doNothing().when(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        doNothing().when(routeEntity).setDescription(Mockito.<String>any());
        doNothing().when(routeEntity).setGpxCoordinates(Mockito.<String>any());
        doNothing().when(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        doNothing().when(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        doNothing().when(routeEntity).setName(Mockito.<String>any());
        doNothing().when(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        doNothing().when(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        doNothing().when(routeEntity).setVideoUrl(Mockito.<String>any());
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");
        when(cloudinaryService.upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any()))
                .thenReturn(pictureEntity);
        routeService.addPicture(1L, new PictureUploadPayloadDTO());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity).getPictures();
        verify(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setId(Mockito.<Long>any());
        verify(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setAuthor(Mockito.<UserEntity>any());
        verify(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        verify(routeEntity).setDescription(Mockito.<String>any());
        verify(routeEntity).setGpxCoordinates(Mockito.<String>any());
        verify(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        verify(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        verify(routeEntity).setName(Mockito.<String>any());
        verify(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        verify(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        verify(routeEntity).setVideoUrl(Mockito.<String>any());
        verify(cloudinaryService).upload(Mockito.<PictureUploadPayloadDTO>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link RouteService#deleteMainPicture(Long)}
     */
    @Test
    public void testDeleteMainPicture() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);

        PictureEntity profilePicture2 = new PictureEntity();
        profilePicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setId(1L);
        profilePicture2.setPublicId("42");
        profilePicture2.setTitle("Dr");
        profilePicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setUrl("https://example.org/example");

        UserEntity author2 = new UserEntity();
        author2.setAccountExpired(true);
        author2.setBirthdate(LocalDate.of(1970, 1, 1));
        author2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setCredentialsExpired(true);
        author2.setEmail("jane.doe@example.org");
        author2.setEnabled(true);
        author2.setFirstName("Jane");
        author2.setId(1L);
        author2.setLastName("Doe");
        author2.setLocked(true);
        author2.setPassword("iloveyou");
        author2.setProfilePicture(profilePicture2);
        author2.setRoles(new HashSet<>());
        author2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setUsername("janedoe");

        PictureEntity mainPicture2 = new PictureEntity();
        mainPicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setId(1L);
        mainPicture2.setPublicId("42");
        mainPicture2.setTitle("Dr");
        mainPicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setUrl("https://example.org/example");

        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setAuthor(author2);
        routeEntity2.setCategories(new HashSet<>());
        routeEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setDescription("The characteristics of someone or something");
        routeEntity2.setGpxCoordinates("Gpx Coordinates");
        routeEntity2.setId(1L);
        routeEntity2.setLevelType(LevelEnum.BEGINNER);
        routeEntity2.setMainPicture(mainPicture2);
        routeEntity2.setName("Name");
        routeEntity2.setPictures(new HashSet<>());
        routeEntity2.setReactions(new HashSet<>());
        routeEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setVideoUrl("https://example.org/example");
        when(routeRepository.save(Mockito.<RouteEntity>any())).thenReturn(routeEntity2);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenReturn(true);
        routeService.deleteMainPicture(1L);
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteService#deleteMainPicture(Long)}
     */
    @Test
    public void testDeleteMainPicture2() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);

        PictureEntity profilePicture2 = new PictureEntity();
        profilePicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setId(1L);
        profilePicture2.setPublicId("42");
        profilePicture2.setTitle("Dr");
        profilePicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setUrl("https://example.org/example");

        UserEntity author2 = new UserEntity();
        author2.setAccountExpired(true);
        author2.setBirthdate(LocalDate.of(1970, 1, 1));
        author2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setCredentialsExpired(true);
        author2.setEmail("jane.doe@example.org");
        author2.setEnabled(true);
        author2.setFirstName("Jane");
        author2.setId(1L);
        author2.setLastName("Doe");
        author2.setLocked(true);
        author2.setPassword("iloveyou");
        author2.setProfilePicture(profilePicture2);
        author2.setRoles(new HashSet<>());
        author2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setUsername("janedoe");

        PictureEntity mainPicture2 = new PictureEntity();
        mainPicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setId(1L);
        mainPicture2.setPublicId("42");
        mainPicture2.setTitle("Dr");
        mainPicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setUrl("https://example.org/example");

        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setAuthor(author2);
        routeEntity2.setCategories(new HashSet<>());
        routeEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setDescription("The characteristics of someone or something");
        routeEntity2.setGpxCoordinates("Gpx Coordinates");
        routeEntity2.setId(1L);
        routeEntity2.setLevelType(LevelEnum.BEGINNER);
        routeEntity2.setMainPicture(mainPicture2);
        routeEntity2.setName("Name");
        routeEntity2.setPictures(new HashSet<>());
        routeEntity2.setReactions(new HashSet<>());
        routeEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setVideoUrl("https://example.org/example");
        when(routeRepository.save(Mockito.<RouteEntity>any())).thenReturn(routeEntity2);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenThrow(new CategoryNotFoundException());
        assertThrows(CategoryNotFoundException.class, () -> routeService.deleteMainPicture(1L));
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteService#deleteMainPicture(Long)}
     */
    @Test
    public void testDeleteMainPicture3() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getMainPicture()).thenReturn(pictureEntity);
        doNothing().when(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setId(Mockito.<Long>any());
        doNothing().when(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setAuthor(Mockito.<UserEntity>any());
        doNothing().when(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        doNothing().when(routeEntity).setDescription(Mockito.<String>any());
        doNothing().when(routeEntity).setGpxCoordinates(Mockito.<String>any());
        doNothing().when(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        doNothing().when(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        doNothing().when(routeEntity).setName(Mockito.<String>any());
        doNothing().when(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        doNothing().when(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        doNothing().when(routeEntity).setVideoUrl(Mockito.<String>any());
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);

        PictureEntity profilePicture2 = new PictureEntity();
        profilePicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setId(1L);
        profilePicture2.setPublicId("42");
        profilePicture2.setTitle("Dr");
        profilePicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setUrl("https://example.org/example");

        UserEntity author2 = new UserEntity();
        author2.setAccountExpired(true);
        author2.setBirthdate(LocalDate.of(1970, 1, 1));
        author2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setCredentialsExpired(true);
        author2.setEmail("jane.doe@example.org");
        author2.setEnabled(true);
        author2.setFirstName("Jane");
        author2.setId(1L);
        author2.setLastName("Doe");
        author2.setLocked(true);
        author2.setPassword("iloveyou");
        author2.setProfilePicture(profilePicture2);
        author2.setRoles(new HashSet<>());
        author2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setUsername("janedoe");

        PictureEntity mainPicture2 = new PictureEntity();
        mainPicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setId(1L);
        mainPicture2.setPublicId("42");
        mainPicture2.setTitle("Dr");
        mainPicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setUrl("https://example.org/example");

        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setAuthor(author2);
        routeEntity2.setCategories(new HashSet<>());
        routeEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setDescription("The characteristics of someone or something");
        routeEntity2.setGpxCoordinates("Gpx Coordinates");
        routeEntity2.setId(1L);
        routeEntity2.setLevelType(LevelEnum.BEGINNER);
        routeEntity2.setMainPicture(mainPicture2);
        routeEntity2.setName("Name");
        routeEntity2.setPictures(new HashSet<>());
        routeEntity2.setReactions(new HashSet<>());
        routeEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setVideoUrl("https://example.org/example");
        when(routeRepository.save(Mockito.<RouteEntity>any())).thenReturn(routeEntity2);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenReturn(true);
        routeService.deleteMainPicture(1L);
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity).getMainPicture();
        verify(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setId(Mockito.<Long>any());
        verify(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setAuthor(Mockito.<UserEntity>any());
        verify(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        verify(routeEntity).setDescription(Mockito.<String>any());
        verify(routeEntity).setGpxCoordinates(Mockito.<String>any());
        verify(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        verify(routeEntity, atLeast(1)).setMainPicture(Mockito.<PictureEntity>any());
        verify(routeEntity).setName(Mockito.<String>any());
        verify(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        verify(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        verify(routeEntity).setVideoUrl(Mockito.<String>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteService#deleteAPicture(Long, Long)}
     */
    @Test
    public void testDeleteAPicture() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");

        HashSet<PictureEntity> pictureEntitySet = new HashSet<>();
        pictureEntitySet.add(pictureEntity);
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getPictures()).thenReturn(pictureEntitySet);
        doNothing().when(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setId(Mockito.<Long>any());
        doNothing().when(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setAuthor(Mockito.<UserEntity>any());
        doNothing().when(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        doNothing().when(routeEntity).setDescription(Mockito.<String>any());
        doNothing().when(routeEntity).setGpxCoordinates(Mockito.<String>any());
        doNothing().when(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        doNothing().when(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        doNothing().when(routeEntity).setName(Mockito.<String>any());
        doNothing().when(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        doNothing().when(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        doNothing().when(routeEntity).setVideoUrl(Mockito.<String>any());
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);

        PictureEntity profilePicture2 = new PictureEntity();
        profilePicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setId(1L);
        profilePicture2.setPublicId("42");
        profilePicture2.setTitle("Dr");
        profilePicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setUrl("https://example.org/example");

        UserEntity author2 = new UserEntity();
        author2.setAccountExpired(true);
        author2.setBirthdate(LocalDate.of(1970, 1, 1));
        author2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setCredentialsExpired(true);
        author2.setEmail("jane.doe@example.org");
        author2.setEnabled(true);
        author2.setFirstName("Jane");
        author2.setId(1L);
        author2.setLastName("Doe");
        author2.setLocked(true);
        author2.setPassword("iloveyou");
        author2.setProfilePicture(profilePicture2);
        author2.setRoles(new HashSet<>());
        author2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setUsername("janedoe");

        PictureEntity mainPicture2 = new PictureEntity();
        mainPicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setId(1L);
        mainPicture2.setPublicId("42");
        mainPicture2.setTitle("Dr");
        mainPicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setUrl("https://example.org/example");

        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setAuthor(author2);
        routeEntity2.setCategories(new HashSet<>());
        routeEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setDescription("The characteristics of someone or something");
        routeEntity2.setGpxCoordinates("Gpx Coordinates");
        routeEntity2.setId(1L);
        routeEntity2.setLevelType(LevelEnum.BEGINNER);
        routeEntity2.setMainPicture(mainPicture2);
        routeEntity2.setName("Name");
        routeEntity2.setPictures(new HashSet<>());
        routeEntity2.setReactions(new HashSet<>());
        routeEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setVideoUrl("https://example.org/example");
        when(routeRepository.save(Mockito.<RouteEntity>any())).thenReturn(routeEntity2);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenReturn(true);
        routeService.deleteAPicture(1L, 1L);
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity, atLeast(1)).getPictures();
        verify(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setId(Mockito.<Long>any());
        verify(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setAuthor(Mockito.<UserEntity>any());
        verify(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        verify(routeEntity).setDescription(Mockito.<String>any());
        verify(routeEntity).setGpxCoordinates(Mockito.<String>any());
        verify(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        verify(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        verify(routeEntity).setName(Mockito.<String>any());
        verify(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        verify(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        verify(routeEntity).setVideoUrl(Mockito.<String>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteService#deleteAPicture(Long, Long)}
     */
    @Test
    public void testDeleteAPicture2() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");

        HashSet<PictureEntity> pictureEntitySet = new HashSet<>();
        pictureEntitySet.add(pictureEntity);
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getPictures()).thenReturn(pictureEntitySet);
        doNothing().when(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setId(Mockito.<Long>any());
        doNothing().when(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setAuthor(Mockito.<UserEntity>any());
        doNothing().when(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        doNothing().when(routeEntity).setDescription(Mockito.<String>any());
        doNothing().when(routeEntity).setGpxCoordinates(Mockito.<String>any());
        doNothing().when(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        doNothing().when(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        doNothing().when(routeEntity).setName(Mockito.<String>any());
        doNothing().when(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        doNothing().when(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        doNothing().when(routeEntity).setVideoUrl(Mockito.<String>any());
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);

        PictureEntity profilePicture2 = new PictureEntity();
        profilePicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setId(1L);
        profilePicture2.setPublicId("42");
        profilePicture2.setTitle("Dr");
        profilePicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setUrl("https://example.org/example");

        UserEntity author2 = new UserEntity();
        author2.setAccountExpired(true);
        author2.setBirthdate(LocalDate.of(1970, 1, 1));
        author2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setCredentialsExpired(true);
        author2.setEmail("jane.doe@example.org");
        author2.setEnabled(true);
        author2.setFirstName("Jane");
        author2.setId(1L);
        author2.setLastName("Doe");
        author2.setLocked(true);
        author2.setPassword("iloveyou");
        author2.setProfilePicture(profilePicture2);
        author2.setRoles(new HashSet<>());
        author2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setUsername("janedoe");

        PictureEntity mainPicture2 = new PictureEntity();
        mainPicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setId(1L);
        mainPicture2.setPublicId("42");
        mainPicture2.setTitle("Dr");
        mainPicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setUrl("https://example.org/example");

        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setAuthor(author2);
        routeEntity2.setCategories(new HashSet<>());
        routeEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setDescription("The characteristics of someone or something");
        routeEntity2.setGpxCoordinates("Gpx Coordinates");
        routeEntity2.setId(1L);
        routeEntity2.setLevelType(LevelEnum.BEGINNER);
        routeEntity2.setMainPicture(mainPicture2);
        routeEntity2.setName("Name");
        routeEntity2.setPictures(new HashSet<>());
        routeEntity2.setReactions(new HashSet<>());
        routeEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setVideoUrl("https://example.org/example");
        when(routeRepository.save(Mockito.<RouteEntity>any())).thenReturn(routeEntity2);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenThrow(new CategoryNotFoundException());
        assertThrows(CategoryNotFoundException.class, () -> routeService.deleteAPicture(1L, 1L));
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity, atLeast(1)).getPictures();
        verify(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setId(Mockito.<Long>any());
        verify(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setAuthor(Mockito.<UserEntity>any());
        verify(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        verify(routeEntity).setDescription(Mockito.<String>any());
        verify(routeEntity).setGpxCoordinates(Mockito.<String>any());
        verify(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        verify(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        verify(routeEntity).setName(Mockito.<String>any());
        verify(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        verify(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        verify(routeEntity).setVideoUrl(Mockito.<String>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteService#deleteAPicture(Long, Long)}
     */
    @Test
    public void testDeleteAPicture3() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");

        PictureEntity pictureEntity2 = new PictureEntity();
        pictureEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity2.setId(2L);
        pictureEntity2.setPublicId("Public Id");
        pictureEntity2.setTitle("Mr");
        pictureEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity2.setUrl("Url");

        HashSet<PictureEntity> pictureEntitySet = new HashSet<>();
        pictureEntitySet.add(pictureEntity2);
        pictureEntitySet.add(pictureEntity);
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getPictures()).thenReturn(pictureEntitySet);
        doNothing().when(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setId(Mockito.<Long>any());
        doNothing().when(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setAuthor(Mockito.<UserEntity>any());
        doNothing().when(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        doNothing().when(routeEntity).setDescription(Mockito.<String>any());
        doNothing().when(routeEntity).setGpxCoordinates(Mockito.<String>any());
        doNothing().when(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        doNothing().when(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        doNothing().when(routeEntity).setName(Mockito.<String>any());
        doNothing().when(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        doNothing().when(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        doNothing().when(routeEntity).setVideoUrl(Mockito.<String>any());
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);

        PictureEntity profilePicture2 = new PictureEntity();
        profilePicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setId(1L);
        profilePicture2.setPublicId("42");
        profilePicture2.setTitle("Dr");
        profilePicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture2.setUrl("https://example.org/example");

        UserEntity author2 = new UserEntity();
        author2.setAccountExpired(true);
        author2.setBirthdate(LocalDate.of(1970, 1, 1));
        author2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setCredentialsExpired(true);
        author2.setEmail("jane.doe@example.org");
        author2.setEnabled(true);
        author2.setFirstName("Jane");
        author2.setId(1L);
        author2.setLastName("Doe");
        author2.setLocked(true);
        author2.setPassword("iloveyou");
        author2.setProfilePicture(profilePicture2);
        author2.setRoles(new HashSet<>());
        author2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author2.setUsername("janedoe");

        PictureEntity mainPicture2 = new PictureEntity();
        mainPicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setId(1L);
        mainPicture2.setPublicId("42");
        mainPicture2.setTitle("Dr");
        mainPicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setUrl("https://example.org/example");

        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setAuthor(author2);
        routeEntity2.setCategories(new HashSet<>());
        routeEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setDescription("The characteristics of someone or something");
        routeEntity2.setGpxCoordinates("Gpx Coordinates");
        routeEntity2.setId(1L);
        routeEntity2.setLevelType(LevelEnum.BEGINNER);
        routeEntity2.setMainPicture(mainPicture2);
        routeEntity2.setName("Name");
        routeEntity2.setPictures(new HashSet<>());
        routeEntity2.setReactions(new HashSet<>());
        routeEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setVideoUrl("https://example.org/example");
        when(routeRepository.save(Mockito.<RouteEntity>any())).thenReturn(routeEntity2);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenReturn(true);
        routeService.deleteAPicture(1L, 1L);
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity, atLeast(1)).getPictures();
        verify(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setId(Mockito.<Long>any());
        verify(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setAuthor(Mockito.<UserEntity>any());
        verify(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        verify(routeEntity).setDescription(Mockito.<String>any());
        verify(routeEntity).setGpxCoordinates(Mockito.<String>any());
        verify(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        verify(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        verify(routeEntity).setName(Mockito.<String>any());
        verify(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        verify(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        verify(routeEntity).setVideoUrl(Mockito.<String>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteService#deleteARoute(Long)}
     */
    @Test
    public void testDeleteARoute() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);
        doNothing().when(routeRepository).delete(Mockito.<RouteEntity>any());
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenReturn(true);
        routeService.deleteARoute(1L);
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeRepository).delete(Mockito.<RouteEntity>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteService#deleteARoute(Long)}
     */
    @Test
    public void testDeleteARoute2() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenThrow(new CategoryNotFoundException());
        assertThrows(CategoryNotFoundException.class, () -> routeService.deleteARoute(1L));
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteService#deleteARoute(Long)}
     */
    @Test
    public void testDeleteARoute3() {
        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

        UserEntity author = new UserEntity();
        author.setAccountExpired(true);
        author.setBirthdate(LocalDate.of(1970, 1, 1));
        author.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setCredentialsExpired(true);
        author.setEmail("jane.doe@example.org");
        author.setEnabled(true);
        author.setFirstName("Jane");
        author.setId(1L);
        author.setLastName("Doe");
        author.setLocked(true);
        author.setPassword("iloveyou");
        author.setProfilePicture(profilePicture);
        author.setRoles(new HashSet<>());
        author.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getMainPicture()).thenReturn(pictureEntity);
        when(routeEntity.getPictures()).thenReturn(new HashSet<>());
        doNothing().when(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setId(Mockito.<Long>any());
        doNothing().when(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(routeEntity).setAuthor(Mockito.<UserEntity>any());
        doNothing().when(routeEntity).setCategories(Mockito.<Set<CategoryEntity>>any());
        doNothing().when(routeEntity).setDescription(Mockito.<String>any());
        doNothing().when(routeEntity).setGpxCoordinates(Mockito.<String>any());
        doNothing().when(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        doNothing().when(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        doNothing().when(routeEntity).setName(Mockito.<String>any());
        doNothing().when(routeEntity).setPictures(Mockito.<Set<PictureEntity>>any());
        doNothing().when(routeEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        doNothing().when(routeEntity).setVideoUrl(Mockito.<String>any());
        routeEntity.setAuthor(author);
        routeEntity.setCategories(new HashSet<>());
        routeEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setDescription("The characteristics of someone or something");
        routeEntity.setGpxCoordinates("Gpx Coordinates");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Name");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);
        doNothing().when(routeRepository).delete(Mockito.<RouteEntity>any());
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenReturn(true);
        routeService.deleteARoute(1L);
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeRepository).delete(Mockito.<RouteEntity>any());
        verify(routeEntity).getMainPicture();
        verify(routeEntity).getPictures();
        verify(routeEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity).setId(Mockito.<Long>any());
        verify(routeEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        verify(routeEntity, atLeast(1)).setAuthor(Mockito.<UserEntity>any());
        verify(routeEntity, atLeast(1)).setCategories(Mockito.<Set<CategoryEntity>>any());
        verify(routeEntity).setDescription(Mockito.<String>any());
        verify(routeEntity).setGpxCoordinates(Mockito.<String>any());
        verify(routeEntity).setLevelType(Mockito.<LevelEnum>any());
        verify(routeEntity, atLeast(1)).setMainPicture(Mockito.<PictureEntity>any());
        verify(routeEntity).setName(Mockito.<String>any());
        verify(routeEntity, atLeast(1)).setPictures(Mockito.<Set<PictureEntity>>any());
        verify(routeEntity, atLeast(1)).setReactions(Mockito.<Set<ReactionEntity>>any());
        verify(routeEntity).setVideoUrl(Mockito.<String>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }
}

