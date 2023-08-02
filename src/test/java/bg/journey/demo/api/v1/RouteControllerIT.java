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

import bg.journey.demo.dto.payload.RouteCreateDTO;
import bg.journey.demo.dto.response.CategoryDTO;
import bg.journey.demo.dto.response.PictureDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.dto.response.RouteDetailsViewDTO;
import bg.journey.demo.dto.response.RouteViewDTO;
import bg.journey.demo.model.entity.CategoryEntity;
import bg.journey.demo.model.entity.PictureEntity;
import bg.journey.demo.model.entity.ReactionEntity;
import bg.journey.demo.model.entity.RouteEntity;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.model.enums.CategoryType;
import bg.journey.demo.model.enums.LevelEnum;
import bg.journey.demo.model.enums.ReactionTargetType;
import bg.journey.demo.model.mapper.CategoryMapper;
import bg.journey.demo.model.mapper.PictureMapper;
import bg.journey.demo.model.mapper.ReactionMapper;
import bg.journey.demo.repository.CategoryRepository;
import bg.journey.demo.repository.PictureRepository;
import bg.journey.demo.repository.ReactionRepository;
import bg.journey.demo.repository.RouteRepository;
import bg.journey.demo.repository.UserRepository;
import bg.journey.demo.security.UserPrincipal;
import bg.journey.demo.service.CloudinaryService;
import bg.journey.demo.service.RouteService;
import com.cloudinary.Cloudinary;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Ignore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {RouteController.class, RouteService.class, CloudinaryService.class, Cloudinary.class})
@ActiveProfiles({"development"})
@RunWith(SpringJUnit4ClassRunner.class)
public class RouteControllerIT {
    @MockBean
    private CategoryMapper categoryMapper;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private PictureMapper pictureMapper;

    @MockBean
    private PictureRepository pictureRepository;

    @MockBean
    private ReactionMapper reactionMapper;

    @MockBean
    private ReactionRepository reactionRepository;

    @Autowired
    private RouteController routeController;

    @MockBean
    private RouteRepository routeRepository;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link RouteController#getAllRoutes()}
     */
    @Test
    public void testGetAllRoutes() {

        RouteRepository routeRepository = mock(RouteRepository.class);
        ArrayList<RouteEntity> routeEntityList = new ArrayList<>();
        when(routeRepository.findAll()).thenReturn(routeEntityList);
        UserRepository userRepository = mock(UserRepository.class);
        CategoryMapper categoryMapper = mock(CategoryMapper.class);
        PictureMapper pictureMapper = mock(PictureMapper.class);
        ReactionMapper reactionMapper = mock(ReactionMapper.class);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        ResponseEntity<ResponseDTO<List<RouteViewDTO>>> actualAllRoutes = (new RouteController(new RouteService(
                routeRepository, userRepository, categoryMapper, pictureMapper, reactionMapper, categoryRepository,
                reactionRepository, new CloudinaryService(new Cloudinary(), mock(PictureRepository.class))))).getAllRoutes();
        assertTrue(actualAllRoutes.hasBody());
        assertTrue(actualAllRoutes.getHeaders().isEmpty());
        assertEquals(200, actualAllRoutes.getStatusCodeValue());
        ResponseDTO<List<RouteViewDTO>> body = actualAllRoutes.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully returned all routes!", body.getMessage());
        assertEquals(routeEntityList, body.getContent());
        verify(routeRepository).findAll();
    }

    /**
     * Method under test: {@link RouteController#getAllRoutes()}
     */
    @Test
    public void testGetAllRoutes2() {

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
        routeEntity.setGpxCoordinates("Successfully returned all routes!");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Successfully returned all routes!");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");

        ArrayList<RouteEntity> routeEntityList = new ArrayList<>();
        routeEntityList.add(routeEntity);
        RouteRepository routeRepository = mock(RouteRepository.class);
        when(routeRepository.findAll()).thenReturn(routeEntityList);
        PictureMapper pictureMapper = mock(PictureMapper.class);
        when(pictureMapper.pictureEntityToPictureDTO(Mockito.<PictureEntity>any()))
                .thenReturn(new PictureDTO("Dr", "42", "https://example.org/example"));
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.findAllByRoutes(Mockito.<RouteEntity>any())).thenReturn(new HashSet<>());
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        when(reactionRepository.findAllByTargetEntityIdAndReactionTargetType(Mockito.<Long>any(),
                Mockito.<ReactionTargetType>any())).thenReturn(new HashSet<>());
        UserRepository userRepository = mock(UserRepository.class);
        CategoryMapper categoryMapper = mock(CategoryMapper.class);
        ReactionMapper reactionMapper = mock(ReactionMapper.class);
        ResponseEntity<ResponseDTO<List<RouteViewDTO>>> actualAllRoutes = (new RouteController(new RouteService(
                routeRepository, userRepository, categoryMapper, pictureMapper, reactionMapper, categoryRepository,
                reactionRepository, new CloudinaryService(new Cloudinary(), mock(PictureRepository.class))))).getAllRoutes();
        assertTrue(actualAllRoutes.hasBody());
        assertTrue(actualAllRoutes.getHeaders().isEmpty());
        assertEquals(200, actualAllRoutes.getStatusCodeValue());
        ResponseDTO<List<RouteViewDTO>> body = actualAllRoutes.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully returned all routes!", body.getMessage());
        assertEquals(1, body.getContent().size());
        verify(routeRepository).findAll();
        verify(pictureMapper).pictureEntityToPictureDTO(Mockito.<PictureEntity>any());
        verify(categoryRepository).findAllByRoutes(Mockito.<RouteEntity>any());
        verify(reactionRepository).findAllByTargetEntityIdAndReactionTargetType(Mockito.<Long>any(),
                Mockito.<ReactionTargetType>any());
    }

    /**
     * Method under test: {@link RouteController#getAllRoutes()}
     */
    @Test
    public void testGetAllRoutes3() {

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

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getMainPicture()).thenReturn(pictureEntity);
        when(routeEntity.getAuthor()).thenReturn(userEntity);
        when(routeEntity.getLevelType()).thenReturn(LevelEnum.BEGINNER);
        when(routeEntity.getId()).thenReturn(1L);
        when(routeEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(routeEntity.getName()).thenReturn("Name");
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
        routeEntity.setGpxCoordinates("Successfully returned all routes!");
        routeEntity.setId(1L);
        routeEntity.setLevelType(LevelEnum.BEGINNER);
        routeEntity.setMainPicture(mainPicture);
        routeEntity.setName("Successfully returned all routes!");
        routeEntity.setPictures(new HashSet<>());
        routeEntity.setReactions(new HashSet<>());
        routeEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity.setVideoUrl("https://example.org/example");

        ArrayList<RouteEntity> routeEntityList = new ArrayList<>();
        routeEntityList.add(routeEntity);
        RouteRepository routeRepository = mock(RouteRepository.class);
        when(routeRepository.findAll()).thenReturn(routeEntityList);
        PictureMapper pictureMapper = mock(PictureMapper.class);
        when(pictureMapper.pictureEntityToPictureDTO(Mockito.<PictureEntity>any()))
                .thenReturn(new PictureDTO("Dr", "42", "https://example.org/example"));
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.findAllByRoutes(Mockito.<RouteEntity>any())).thenReturn(new HashSet<>());
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        when(reactionRepository.findAllByTargetEntityIdAndReactionTargetType(Mockito.<Long>any(),
                Mockito.<ReactionTargetType>any())).thenReturn(new HashSet<>());
        UserRepository userRepository = mock(UserRepository.class);
        CategoryMapper categoryMapper = mock(CategoryMapper.class);
        ReactionMapper reactionMapper = mock(ReactionMapper.class);
        ResponseEntity<ResponseDTO<List<RouteViewDTO>>> actualAllRoutes = (new RouteController(new RouteService(
                routeRepository, userRepository, categoryMapper, pictureMapper, reactionMapper, categoryRepository,
                reactionRepository, new CloudinaryService(new Cloudinary(), mock(PictureRepository.class))))).getAllRoutes();
        assertTrue(actualAllRoutes.hasBody());
        assertTrue(actualAllRoutes.getHeaders().isEmpty());
        assertEquals(200, actualAllRoutes.getStatusCodeValue());
        ResponseDTO<List<RouteViewDTO>> body = actualAllRoutes.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully returned all routes!", body.getMessage());
        assertEquals(1, body.getContent().size());
        verify(routeRepository).findAll();
        verify(routeEntity).getMainPicture();
        verify(routeEntity).getAuthor();
        verify(routeEntity).getLevelType();
        verify(routeEntity, atLeast(1)).getId();
        verify(routeEntity).getDescription();
        verify(routeEntity).getName();
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
        verify(pictureMapper).pictureEntityToPictureDTO(Mockito.<PictureEntity>any());
        verify(categoryRepository).findAllByRoutes(Mockito.<RouteEntity>any());
        verify(reactionRepository).findAllByTargetEntityIdAndReactionTargetType(Mockito.<Long>any(),
                Mockito.<ReactionTargetType>any());
    }

    /**
     * Method under test: {@link RouteController#getRoute(Long)}
     */
    @Test
    public void testGetRoute() {

        PictureEntity profilePicture = new PictureEntity();
        profilePicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setId(1L);
        profilePicture.setPublicId("42");
        profilePicture.setTitle("Dr");
        profilePicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture.setUrl("https://example.org/example");

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
        userEntity.setProfilePicture(profilePicture);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");

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
        when(routeEntity.getAuthor()).thenReturn(userEntity);
        when(routeEntity.getLevelType()).thenReturn(LevelEnum.BEGINNER);
        when(routeEntity.getId()).thenReturn(1L);
        when(routeEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(routeEntity.getGpxCoordinates()).thenReturn("Gpx Coordinates");
        when(routeEntity.getName()).thenReturn("Name");
        when(routeEntity.getVideoUrl()).thenReturn("https://example.org/example");
        RouteRepository routeRepository = mock(RouteRepository.class);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(routeEntity));
        PictureMapper pictureMapper = mock(PictureMapper.class);
        PictureDTO pictureDTO = new PictureDTO("Dr", "42", "https://example.org/example");

        when(pictureMapper.pictureEntityToPictureDTO(Mockito.<PictureEntity>any())).thenReturn(pictureDTO);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.findAllByRoutes(Mockito.<RouteEntity>any())).thenReturn(new HashSet<>());
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        when(reactionRepository.findAllByTargetEntityIdAndReactionTargetType(Mockito.<Long>any(),
                Mockito.<ReactionTargetType>any())).thenReturn(new HashSet<>());
        UserRepository userRepository = mock(UserRepository.class);
        CategoryMapper categoryMapper = mock(CategoryMapper.class);
        ReactionMapper reactionMapper = mock(ReactionMapper.class);
        ResponseEntity<ResponseDTO<RouteDetailsViewDTO>> actualRoute = (new RouteController(new RouteService(
                routeRepository, userRepository, categoryMapper, pictureMapper, reactionMapper, categoryRepository,
                reactionRepository, new CloudinaryService(new Cloudinary(), mock(PictureRepository.class))))).getRoute(1L);
        assertTrue(actualRoute.hasBody());
        assertTrue(actualRoute.getHeaders().isEmpty());
        assertEquals(200, actualRoute.getStatusCodeValue());
        ResponseDTO<RouteDetailsViewDTO> body = actualRoute.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully returned route by id!", body.getMessage());
        RouteDetailsViewDTO content = body.getContent();
        assertEquals("janedoe", content.getAuthorUsername());
        assertEquals("https://example.org/example", content.getVideoUrl());
        assertTrue(content.getReactions().isEmpty());
        assertTrue(content.getPictures().isEmpty());
        assertEquals("Name", content.getName());
        assertSame(pictureDTO, content.getMainPicture());
        assertEquals(LevelEnum.BEGINNER, content.getLevelType());
        assertTrue(content.getCategories().isEmpty());
        assertEquals("Gpx Coordinates", content.getGpxCoordinates());
        assertEquals(1L, content.getId().longValue());
        assertEquals("The characteristics of someone or something", content.getDescription());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity).getMainPicture();
        verify(routeEntity).getAuthor();
        verify(routeEntity).getLevelType();
        verify(routeEntity, atLeast(1)).getId();
        verify(routeEntity).getDescription();
        verify(routeEntity).getGpxCoordinates();
        verify(routeEntity).getName();
        verify(routeEntity).getVideoUrl();
        verify(routeEntity).getPictures();
        verify(pictureMapper).pictureEntityToPictureDTO(Mockito.<PictureEntity>any());
        verify(categoryRepository).findAllByRoutes(Mockito.<RouteEntity>any());
        verify(reactionRepository).findAllByTargetEntityIdAndReactionTargetType(Mockito.<Long>any(),
                Mockito.<ReactionTargetType>any());
    }

    /**
     * Method under test: {@link RouteController#getRoute(Long)}
     */
    @Test
    public void testGetRoute2() {

        RouteService routeService = mock(RouteService.class);
        RouteDetailsViewDTO routeDetailsViewDTO = new RouteDetailsViewDTO();
        when(routeService.getRouteById(Mockito.<Long>any())).thenReturn(routeDetailsViewDTO);
        ResponseEntity<ResponseDTO<RouteDetailsViewDTO>> actualRoute = (new RouteController(routeService)).getRoute(1L);
        assertTrue(actualRoute.hasBody());
        assertTrue(actualRoute.getHeaders().isEmpty());
        assertEquals(200, actualRoute.getStatusCodeValue());
        ResponseDTO<RouteDetailsViewDTO> body = actualRoute.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully returned route by id!", body.getMessage());
        assertSame(routeDetailsViewDTO, body.getContent());
        verify(routeService).getRouteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link RouteController#addRoutePicture(Long, UserPrincipal, String, MultipartFile)}
     */
    @Test
    public void testAddRoutePicture() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/v1/routes/{routeId}/pictures", 1L);
        MockHttpServletRequestBuilder paramResult = postResult.param("file", String.valueOf((Object) null));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("userPrincipal",
                String.valueOf(new UserPrincipal()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(routeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }

    /**
     * Method under test: {@link RouteController#createRoute(UserPrincipal, RouteCreateDTO, BindingResult)}
     */
    @Test
    public void testCreateRoute() {

        RouteService routeService = mock(RouteService.class);
        doNothing().when(routeService).createRoute(Mockito.<RouteCreateDTO>any(), Mockito.<String>any());
        RouteController routeController = new RouteController(routeService);
        UserPrincipal userPrincipal = new UserPrincipal();
        RouteCreateDTO routeCreateDTO = new RouteCreateDTO();
        ResponseEntity<ResponseDTO<Object>> actualCreateRouteResult = routeController.createRoute(userPrincipal,
                routeCreateDTO, new BindException("Target", "Object Name"));
        assertTrue(actualCreateRouteResult.hasBody());
        assertTrue(actualCreateRouteResult.getHeaders().isEmpty());
        assertEquals(201, actualCreateRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualCreateRouteResult.getBody();
        assertEquals(201, body.getStatus());
        assertEquals("Successfully created a route!", body.getMessage());
        assertNull(body.getContent());
        verify(routeService).createRoute(Mockito.<RouteCreateDTO>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link RouteController#createRoute(UserPrincipal, RouteCreateDTO, BindingResult)}
     */
    @Test
    public void testCreateRoute2() {

        RouteController routeController = new RouteController(mock(RouteService.class));
        UserPrincipal userPrincipal = new UserPrincipal();
        RouteCreateDTO routeCreateDTO = new RouteCreateDTO();
        BeanPropertyBindingResult bindingResult = mock(BeanPropertyBindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());
        when(bindingResult.hasErrors()).thenReturn(true);
        ResponseEntity<ResponseDTO<Object>> actualCreateRouteResult = routeController.createRoute(userPrincipal,
                routeCreateDTO, bindingResult);
        assertTrue(actualCreateRouteResult.hasBody());
        assertTrue(actualCreateRouteResult.getHeaders().isEmpty());
        assertEquals(400, actualCreateRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualCreateRouteResult.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("", body.getMessage());
        assertNull(body.getContent());
        verify(bindingResult).hasErrors();
        verify(bindingResult).getAllErrors();
    }

    /**
     * Method under test: {@link RouteController#getAllCategories()}
     */
    @Test
    public void testGetAllCategories() {

        CategoryMapper categoryMapper = mock(CategoryMapper.class);
        when(categoryMapper.categoryEntityToCategoryDTO(Mockito.<CategoryEntity>any())).thenReturn(new CategoryDTO());

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        categoryEntity.setDescription("The characteristics of someone or something");
        categoryEntity.setId(1L);
        categoryEntity.setName(CategoryType.PEDESTRIAN);
        categoryEntity.setRoutes(new HashSet<>());
        categoryEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<CategoryEntity> categoryEntityList = new ArrayList<>();
        categoryEntityList.add(categoryEntity);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.findAll()).thenReturn(categoryEntityList);
        RouteRepository routeRepository = mock(RouteRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        PictureMapper pictureMapper = mock(PictureMapper.class);
        ReactionMapper reactionMapper = mock(ReactionMapper.class);
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        ResponseEntity<ResponseDTO<Set<CategoryDTO>>> actualAllCategories = (new RouteController(new RouteService(
                routeRepository, userRepository, categoryMapper, pictureMapper, reactionMapper, categoryRepository,
                reactionRepository, new CloudinaryService(new Cloudinary(), mock(PictureRepository.class)))))
                .getAllCategories();
        assertTrue(actualAllCategories.hasBody());
        assertTrue(actualAllCategories.getHeaders().isEmpty());
        assertEquals(200, actualAllCategories.getStatusCodeValue());
        ResponseDTO<Set<CategoryDTO>> body = actualAllCategories.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully returned all categories!", body.getMessage());
        assertEquals(1, body.getContent().size());
        verify(categoryMapper).categoryEntityToCategoryDTO(Mockito.<CategoryEntity>any());
        verify(categoryRepository).findAll();
    }

    /**
     * Method under test: {@link RouteController#getAllCategories()}
     */
    @Test
    public void testGetAllCategories2() {

        RouteService routeService = mock(RouteService.class);
        when(routeService.getAllCategories()).thenReturn(new HashSet<>());
        ResponseEntity<ResponseDTO<Set<CategoryDTO>>> actualAllCategories = (new RouteController(routeService))
                .getAllCategories();
        assertTrue(actualAllCategories.hasBody());
        assertTrue(actualAllCategories.getHeaders().isEmpty());
        assertEquals(200, actualAllCategories.getStatusCodeValue());
        ResponseDTO<Set<CategoryDTO>> body = actualAllCategories.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully returned all categories!", body.getMessage());
        assertTrue(body.getContent().isEmpty());
        verify(routeService).getAllCategories();
    }

    /**
     * Method under test: {@link RouteController#deleteRouteMainPicture(Long, UserPrincipal)}
     */
    @Test
    public void testDeleteRouteMainPicture() {

        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setId(1L);
        pictureEntity.setPublicId("42");
        pictureEntity.setTitle("Dr");
        pictureEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        pictureEntity.setUrl("https://example.org/example");
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getMainPicture()).thenReturn(pictureEntity);
        doNothing().when(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        Optional<RouteEntity> ofResult = Optional.of(routeEntity);

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

        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setAuthor(author);
        routeEntity2.setCategories(new HashSet<>());
        routeEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setDescription("The characteristics of someone or something");
        routeEntity2.setGpxCoordinates("Gpx Coordinates");
        routeEntity2.setId(1L);
        routeEntity2.setLevelType(LevelEnum.BEGINNER);
        routeEntity2.setMainPicture(mainPicture);
        routeEntity2.setName("Name");
        routeEntity2.setPictures(new HashSet<>());
        routeEntity2.setReactions(new HashSet<>());
        routeEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        routeEntity2.setVideoUrl("https://example.org/example");
        RouteRepository routeRepository = mock(RouteRepository.class);
        when(routeRepository.save(Mockito.<RouteEntity>any())).thenReturn(routeEntity2);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        CloudinaryService cloudinaryService = mock(CloudinaryService.class);
        when(cloudinaryService.delete(Mockito.<PictureEntity>any())).thenReturn(true);
        RouteController routeController = new RouteController(new RouteService(routeRepository,
                mock(UserRepository.class), mock(CategoryMapper.class), mock(PictureMapper.class), mock(ReactionMapper.class),
                mock(CategoryRepository.class), mock(ReactionRepository.class), cloudinaryService));
        ResponseEntity<ResponseDTO<Object>> actualDeleteRouteMainPictureResult = routeController
                .deleteRouteMainPicture(1L, new UserPrincipal());
        assertTrue(actualDeleteRouteMainPictureResult.hasBody());
        assertTrue(actualDeleteRouteMainPictureResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteRouteMainPictureResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteRouteMainPictureResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Route main picture deleted successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity).getMainPicture();
        verify(routeEntity).setMainPicture(Mockito.<PictureEntity>any());
        verify(cloudinaryService).delete(Mockito.<PictureEntity>any());
    }

    /**
     * Method under test: {@link RouteController#deleteRouteMainPicture(Long, UserPrincipal)}
     */
    @Test
    public void testDeleteRouteMainPicture2() {

        RouteService routeService = mock(RouteService.class);
        doNothing().when(routeService).deleteMainPicture(Mockito.<Long>any());
        RouteController routeController = new RouteController(routeService);
        ResponseEntity<ResponseDTO<Object>> actualDeleteRouteMainPictureResult = routeController
                .deleteRouteMainPicture(1L, new UserPrincipal());
        assertTrue(actualDeleteRouteMainPictureResult.hasBody());
        assertTrue(actualDeleteRouteMainPictureResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteRouteMainPictureResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteRouteMainPictureResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Route main picture deleted successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(routeService).deleteMainPicture(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link RouteController#deleteAPicture(Long, Long, UserPrincipal)}
     */
    @Test
    public void testDeleteAPicture() throws Exception {
        RouteService routeService = mock(RouteService.class);
        doNothing().when(routeService).deleteAPicture(Mockito.<Long>any(), Mockito.<Long>any());
        RouteController routeController = new RouteController(routeService);
        ResponseEntity<ResponseDTO<Object>> actualDeleteRoutePictureResult = routeController
                .deleteAPicture(1L, 1L, new UserPrincipal());
        assertTrue(actualDeleteRoutePictureResult.hasBody());
        assertTrue(actualDeleteRoutePictureResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteRoutePictureResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteRoutePictureResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Route picture deleted successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(routeService).deleteAPicture(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link RouteController#deleteARoute(Long, UserPrincipal)}
     */
    @Test
    public void testDeleteARoute() throws Exception {
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
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/api/v1/routes/{routeId}",
                "Uri Variables", "Uri Variables");
        MockHttpServletRequestBuilder requestBuilder = deleteResult.param("userPrincipal",
                String.valueOf(new UserPrincipal()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(routeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link RouteController#setRouteMainPicture(Long, UserPrincipal, String, MultipartFile)}
     */
    @Test
    public void testSetRouteMainPicture() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/v1/routes/{routeId}/main-picture",
                1L);
        MockHttpServletRequestBuilder paramResult = postResult.param("file", String.valueOf((Object) null));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("userPrincipal",
                String.valueOf(new UserPrincipal()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(routeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }
}

