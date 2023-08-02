package bg.journey.demo.api.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bg.journey.demo.dto.payload.ReactionCreationDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.model.entity.CommentEntity;
import bg.journey.demo.model.entity.PictureEntity;
import bg.journey.demo.model.entity.ReactionEntity;
import bg.journey.demo.model.entity.RouteEntity;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.model.enums.LevelEnum;
import bg.journey.demo.model.enums.ReactionEnum;
import bg.journey.demo.model.enums.ReactionTargetType;
import bg.journey.demo.repository.CommentRepository;
import bg.journey.demo.repository.ReactionRepository;
import bg.journey.demo.repository.RouteRepository;
import bg.journey.demo.repository.UserRepository;
import bg.journey.demo.security.UserPrincipal;
import bg.journey.demo.service.ReactionService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class ReactionControllerIT {
    /**
     * Method under test: {@link ReactionController#reactToRoute(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testReactToRoute() {

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

        ReactionEntity reactionEntity = new ReactionEntity();
        reactionEntity.setAuthor(author);
        reactionEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity.setId(1L);
        reactionEntity.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity.setReactionType(ReactionEnum.LIKE);
        reactionEntity.setTargetEntityId(1L);
        reactionEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        when(reactionRepository.save(Mockito.<ReactionEntity>any())).thenReturn(reactionEntity);
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getId()).thenReturn(1L);
        when(routeEntity.getReactions()).thenReturn(new HashSet<>());
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

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setAuthor(author2);
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

        PictureEntity profilePicture3 = new PictureEntity();
        profilePicture3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setId(1L);
        profilePicture3.setPublicId("42");
        profilePicture3.setTitle("Dr");
        profilePicture3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setUrl("https://example.org/example");

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
        userEntity.setProfilePicture(profilePicture3);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        ReactionController reactionController = new ReactionController(
                new ReactionService(reactionRepository, mock(CommentRepository.class), routeRepository, userRepository));
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.ROUTE);

        ResponseEntity<ResponseDTO<Object>> actualReactToRouteResult = reactionController.reactToRoute(1L, userPrincipal,
                reactionCreationDTO, new BindException("Target", "Object Name"));
        assertTrue(actualReactToRouteResult.hasBody());
        assertTrue(actualReactToRouteResult.getHeaders().isEmpty());
        assertEquals(200, actualReactToRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualReactToRouteResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully react to a route.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionRepository).save(Mockito.<ReactionEntity>any());
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity).getId();
        verify(routeEntity).getReactions();
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link ReactionController#editReactionToRoute(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testEditReactionToRoute() {

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

        ReactionEntity reactionEntity = new ReactionEntity();
        reactionEntity.setAuthor(author);
        reactionEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity.setId(1L);
        reactionEntity.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity.setReactionType(ReactionEnum.LIKE);
        reactionEntity.setTargetEntityId(1L);
        reactionEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ReactionEntity> ofResult = Optional.of(reactionEntity);

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

        ReactionEntity reactionEntity2 = new ReactionEntity();
        reactionEntity2.setAuthor(author2);
        reactionEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity2.setId(1L);
        reactionEntity2.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity2.setReactionType(ReactionEnum.LIKE);
        reactionEntity2.setTargetEntityId(1L);
        reactionEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        when(reactionRepository.save(Mockito.<ReactionEntity>any())).thenReturn(reactionEntity2);
        when(reactionRepository.findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getId()).thenReturn(1L);
        RouteRepository routeRepository = mock(RouteRepository.class);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(routeEntity));

        PictureEntity profilePicture3 = new PictureEntity();
        profilePicture3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setId(1L);
        profilePicture3.setPublicId("42");
        profilePicture3.setTitle("Dr");
        profilePicture3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setUrl("https://example.org/example");

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
        userEntity.setProfilePicture(profilePicture3);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        ReactionController reactionController = new ReactionController(
                new ReactionService(reactionRepository, mock(CommentRepository.class), routeRepository, userRepository));
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.ROUTE);

        ResponseEntity<ResponseDTO<Object>> actualEditReactionToRouteResult = reactionController.editReactionToRoute(1L,
                userPrincipal, reactionCreationDTO, new BindException("Target", "Object Name"));
        assertTrue(actualEditReactionToRouteResult.hasBody());
        assertTrue(actualEditReactionToRouteResult.getHeaders().isEmpty());
        assertEquals(200, actualEditReactionToRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualEditReactionToRouteResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully edit reaction to a route.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionRepository).save(Mockito.<ReactionEntity>any());
        verify(reactionRepository).findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity).getId();
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link ReactionController#editReactionToRoute(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testEditReactionToRoute2() {
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
        ReactionEntity reactionEntity = mock(ReactionEntity.class);
        doNothing().when(reactionEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(reactionEntity).setId(Mockito.<Long>any());
        doNothing().when(reactionEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(reactionEntity).setAuthor(Mockito.<UserEntity>any());
        doNothing().when(reactionEntity).setReactionTargetType(Mockito.<ReactionTargetType>any());
        doNothing().when(reactionEntity).setReactionType(Mockito.<ReactionEnum>any());
        doNothing().when(reactionEntity).setTargetEntityId(Mockito.<Long>any());
        reactionEntity.setAuthor(author);
        reactionEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity.setId(1L);
        reactionEntity.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity.setReactionType(ReactionEnum.LIKE);
        reactionEntity.setTargetEntityId(1L);
        reactionEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ReactionEntity> ofResult = Optional.of(reactionEntity);

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

        ReactionEntity reactionEntity2 = new ReactionEntity();
        reactionEntity2.setAuthor(author2);
        reactionEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity2.setId(1L);
        reactionEntity2.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity2.setReactionType(ReactionEnum.LIKE);
        reactionEntity2.setTargetEntityId(1L);
        reactionEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        when(reactionRepository.save(Mockito.<ReactionEntity>any())).thenReturn(reactionEntity2);
        when(reactionRepository.findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getId()).thenReturn(1L);
        RouteRepository routeRepository = mock(RouteRepository.class);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(routeEntity));

        PictureEntity profilePicture3 = new PictureEntity();
        profilePicture3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setId(1L);
        profilePicture3.setPublicId("42");
        profilePicture3.setTitle("Dr");
        profilePicture3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setUrl("https://example.org/example");

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
        userEntity.setProfilePicture(profilePicture3);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        ReactionController reactionController = new ReactionController(
                new ReactionService(reactionRepository, mock(CommentRepository.class), routeRepository, userRepository));
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.ROUTE);

        ResponseEntity<ResponseDTO<Object>> actualEditReactionToRouteResult = reactionController.editReactionToRoute(1L,
                userPrincipal, reactionCreationDTO, new BindException("Target", "Object Name"));
        assertTrue(actualEditReactionToRouteResult.hasBody());
        assertTrue(actualEditReactionToRouteResult.getHeaders().isEmpty());
        assertEquals(200, actualEditReactionToRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualEditReactionToRouteResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully edit reaction to a route.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionRepository).save(Mockito.<ReactionEntity>any());
        verify(reactionRepository).findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any());
        verify(reactionEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        verify(reactionEntity).setId(Mockito.<Long>any());
        verify(reactionEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        verify(reactionEntity).setAuthor(Mockito.<UserEntity>any());
        verify(reactionEntity).setReactionTargetType(Mockito.<ReactionTargetType>any());
        verify(reactionEntity, atLeast(1)).setReactionType(Mockito.<ReactionEnum>any());
        verify(reactionEntity).setTargetEntityId(Mockito.<Long>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity).getId();
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link ReactionController#editReactionToRoute(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testEditReactionToRoute3() {

        ReactionService reactionService = mock(ReactionService.class);
        doNothing().when(reactionService)
                .editReaction(Mockito.<Long>any(), Mockito.<UserPrincipal>any(), Mockito.<ReactionCreationDTO>any());
        ReactionController reactionController = new ReactionController(reactionService);
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.ROUTE);

        ResponseEntity<ResponseDTO<Object>> actualEditReactionToRouteResult = reactionController.editReactionToRoute(1L,
                userPrincipal, reactionCreationDTO, new BindException("Target", "Object Name"));
        assertTrue(actualEditReactionToRouteResult.hasBody());
        assertTrue(actualEditReactionToRouteResult.getHeaders().isEmpty());
        assertEquals(200, actualEditReactionToRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualEditReactionToRouteResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully edit reaction to a route.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionService).editReaction(Mockito.<Long>any(), Mockito.<UserPrincipal>any(),
                Mockito.<ReactionCreationDTO>any());
    }

    /**
     * Method under test: {@link ReactionController#editReactionToRoute(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testEditReactionToRoute4() {

        ReactionController reactionController = new ReactionController(mock(ReactionService.class));
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.COMMENT);

        ResponseEntity<ResponseDTO<Object>> actualEditReactionToRouteResult = reactionController.editReactionToRoute(1L,
                userPrincipal, reactionCreationDTO, new BindException("Target", "Object Name"));
        assertTrue(actualEditReactionToRouteResult.hasBody());
        assertTrue(actualEditReactionToRouteResult.getHeaders().isEmpty());
        assertEquals(400, actualEditReactionToRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualEditReactionToRouteResult.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("", body.getMessage());
        assertNull(body.getContent());
    }

    /**
     * Method under test: {@link ReactionController#editReactionToRoute(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testEditReactionToRoute5() {

        ReactionController reactionController = new ReactionController(mock(ReactionService.class));
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.ROUTE);

        BeanPropertyBindingResult bindingResult = mock(BeanPropertyBindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());
        when(bindingResult.hasErrors()).thenReturn(true);
        ResponseEntity<ResponseDTO<Object>> actualEditReactionToRouteResult = reactionController.editReactionToRoute(1L,
                userPrincipal, reactionCreationDTO, bindingResult);
        assertTrue(actualEditReactionToRouteResult.hasBody());
        assertTrue(actualEditReactionToRouteResult.getHeaders().isEmpty());
        assertEquals(400, actualEditReactionToRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualEditReactionToRouteResult.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("", body.getMessage());
        assertNull(body.getContent());
        verify(bindingResult).hasErrors();
        verify(bindingResult).getAllErrors();
    }

    /**
     * Method under test: {@link ReactionController#deleteAReactionToRoute(Long, UserPrincipal)}
     */
    @Test
    public void testDeleteAReactionToRoute() {

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

        ReactionEntity reactionEntity = new ReactionEntity();
        reactionEntity.setAuthor(author);
        reactionEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity.setId(1L);
        reactionEntity.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity.setReactionType(ReactionEnum.LIKE);
        reactionEntity.setTargetEntityId(1L);
        reactionEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ReactionEntity> ofResult = Optional.of(reactionEntity);
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        doNothing().when(reactionRepository).delete(Mockito.<ReactionEntity>any());
        when(reactionRepository.findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getId()).thenReturn(1L);
        when(routeEntity.getReactions()).thenReturn(new HashSet<>());
        Optional<RouteEntity> ofResult2 = Optional.of(routeEntity);

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

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setAuthor(author2);
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
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        PictureEntity profilePicture3 = new PictureEntity();
        profilePicture3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setId(1L);
        profilePicture3.setPublicId("42");
        profilePicture3.setTitle("Dr");
        profilePicture3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setUrl("https://example.org/example");

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
        userEntity.setProfilePicture(profilePicture3);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        ReactionController reactionController = new ReactionController(
                new ReactionService(reactionRepository, mock(CommentRepository.class), routeRepository, userRepository));
        ResponseEntity<ResponseDTO<Object>> actualDeleteAReactionToRouteResult = reactionController
                .deleteAReactionToRoute(1L, new UserPrincipal());
        assertTrue(actualDeleteAReactionToRouteResult.hasBody());
        assertTrue(actualDeleteAReactionToRouteResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteAReactionToRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteAReactionToRouteResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully delete a reaction to a route.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionRepository).findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any());
        verify(reactionRepository).delete(Mockito.<ReactionEntity>any());
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity).getId();
        verify(routeEntity).getReactions();
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link ReactionController#deleteAReactionToRoute(Long, UserPrincipal)}
     */
    @Test
    public void testDeleteAReactionToRoute2() {

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

        ReactionEntity reactionEntity = new ReactionEntity();
        reactionEntity.setAuthor(author);
        reactionEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity.setId(1L);
        reactionEntity.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity.setReactionType(ReactionEnum.LIKE);
        reactionEntity.setTargetEntityId(1L);
        reactionEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ReactionEntity> ofResult = Optional.of(reactionEntity);
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        doNothing().when(reactionRepository).delete(Mockito.<ReactionEntity>any());
        when(reactionRepository.findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any()))
                .thenReturn(ofResult);

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

        ReactionEntity reactionEntity2 = new ReactionEntity();
        reactionEntity2.setAuthor(author2);
        reactionEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity2.setId(1L);
        reactionEntity2.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity2.setReactionType(ReactionEnum.LIKE);
        reactionEntity2.setTargetEntityId(1L);
        reactionEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());

        HashSet<ReactionEntity> reactionEntitySet = new HashSet<>();
        reactionEntitySet.add(reactionEntity2);
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getId()).thenReturn(1L);
        when(routeEntity.getReactions()).thenReturn(reactionEntitySet);
        Optional<RouteEntity> ofResult2 = Optional.of(routeEntity);

        PictureEntity profilePicture3 = new PictureEntity();
        profilePicture3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setId(1L);
        profilePicture3.setPublicId("42");
        profilePicture3.setTitle("Dr");
        profilePicture3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setUrl("https://example.org/example");

        UserEntity author3 = new UserEntity();
        author3.setAccountExpired(true);
        author3.setBirthdate(LocalDate.of(1970, 1, 1));
        author3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author3.setCredentialsExpired(true);
        author3.setEmail("jane.doe@example.org");
        author3.setEnabled(true);
        author3.setFirstName("Jane");
        author3.setId(1L);
        author3.setLastName("Doe");
        author3.setLocked(true);
        author3.setPassword("iloveyou");
        author3.setProfilePicture(profilePicture3);
        author3.setRoles(new HashSet<>());
        author3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author3.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setAuthor(author3);
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
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        PictureEntity profilePicture4 = new PictureEntity();
        profilePicture4.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture4.setId(1L);
        profilePicture4.setPublicId("42");
        profilePicture4.setTitle("Dr");
        profilePicture4.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture4.setUrl("https://example.org/example");

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
        userEntity.setProfilePicture(profilePicture4);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        ReactionController reactionController = new ReactionController(
                new ReactionService(reactionRepository, mock(CommentRepository.class), routeRepository, userRepository));
        ResponseEntity<ResponseDTO<Object>> actualDeleteAReactionToRouteResult = reactionController
                .deleteAReactionToRoute(1L, new UserPrincipal());
        assertTrue(actualDeleteAReactionToRouteResult.hasBody());
        assertTrue(actualDeleteAReactionToRouteResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteAReactionToRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteAReactionToRouteResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully delete a reaction to a route.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionRepository).findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any());
        verify(reactionRepository).delete(Mockito.<ReactionEntity>any());
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity).getId();
        verify(routeEntity).getReactions();
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link ReactionController#deleteAReactionToRoute(Long, UserPrincipal)}
     */
    @Test
    public void testDeleteAReactionToRoute3() {

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

        ReactionEntity reactionEntity = new ReactionEntity();
        reactionEntity.setAuthor(author);
        reactionEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity.setId(1L);
        reactionEntity.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity.setReactionType(ReactionEnum.LIKE);
        reactionEntity.setTargetEntityId(1L);
        reactionEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ReactionEntity> ofResult = Optional.of(reactionEntity);
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        doNothing().when(reactionRepository).delete(Mockito.<ReactionEntity>any());
        when(reactionRepository.findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any()))
                .thenReturn(ofResult);

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

        ReactionEntity reactionEntity2 = new ReactionEntity();
        reactionEntity2.setAuthor(author2);
        reactionEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity2.setId(1L);
        reactionEntity2.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity2.setReactionType(ReactionEnum.LIKE);
        reactionEntity2.setTargetEntityId(1L);
        reactionEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());

        PictureEntity profilePicture3 = new PictureEntity();
        profilePicture3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setId(2L);
        profilePicture3.setPublicId("Successfully delete a reaction to a route.");
        profilePicture3.setTitle("Mr");
        profilePicture3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setUrl("Successfully delete a reaction to a route.");

        UserEntity author3 = new UserEntity();
        author3.setAccountExpired(false);
        author3.setBirthdate(LocalDate.of(1970, 1, 1));
        author3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author3.setCredentialsExpired(false);
        author3.setEmail("john.smith@example.org");
        author3.setEnabled(false);
        author3.setFirstName("John");
        author3.setId(2L);
        author3.setLastName("Smith");
        author3.setLocked(false);
        author3.setPassword("Successfully delete a reaction to a route.");
        author3.setProfilePicture(profilePicture3);
        author3.setRoles(new HashSet<>());
        author3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author3.setUsername("Successfully delete a reaction to a route.");

        ReactionEntity reactionEntity3 = new ReactionEntity();
        reactionEntity3.setAuthor(author3);
        reactionEntity3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity3.setId(2L);
        reactionEntity3.setReactionTargetType(ReactionTargetType.COMMENT);
        reactionEntity3.setReactionType(ReactionEnum.DISLIKE);
        reactionEntity3.setTargetEntityId(2L);
        reactionEntity3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());

        HashSet<ReactionEntity> reactionEntitySet = new HashSet<>();
        reactionEntitySet.add(reactionEntity3);
        reactionEntitySet.add(reactionEntity2);
        RouteEntity routeEntity = mock(RouteEntity.class);
        when(routeEntity.getId()).thenReturn(1L);
        when(routeEntity.getReactions()).thenReturn(reactionEntitySet);
        Optional<RouteEntity> ofResult2 = Optional.of(routeEntity);

        PictureEntity profilePicture4 = new PictureEntity();
        profilePicture4.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture4.setId(1L);
        profilePicture4.setPublicId("42");
        profilePicture4.setTitle("Dr");
        profilePicture4.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture4.setUrl("https://example.org/example");

        UserEntity author4 = new UserEntity();
        author4.setAccountExpired(true);
        author4.setBirthdate(LocalDate.of(1970, 1, 1));
        author4.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author4.setCredentialsExpired(true);
        author4.setEmail("jane.doe@example.org");
        author4.setEnabled(true);
        author4.setFirstName("Jane");
        author4.setId(1L);
        author4.setLastName("Doe");
        author4.setLocked(true);
        author4.setPassword("iloveyou");
        author4.setProfilePicture(profilePicture4);
        author4.setRoles(new HashSet<>());
        author4.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author4.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setAuthor(author4);
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
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        PictureEntity profilePicture5 = new PictureEntity();
        profilePicture5.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture5.setId(1L);
        profilePicture5.setPublicId("42");
        profilePicture5.setTitle("Dr");
        profilePicture5.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture5.setUrl("https://example.org/example");

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
        userEntity.setProfilePicture(profilePicture5);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        ReactionController reactionController = new ReactionController(
                new ReactionService(reactionRepository, mock(CommentRepository.class), routeRepository, userRepository));
        ResponseEntity<ResponseDTO<Object>> actualDeleteAReactionToRouteResult = reactionController
                .deleteAReactionToRoute(1L, new UserPrincipal());
        assertTrue(actualDeleteAReactionToRouteResult.hasBody());
        assertTrue(actualDeleteAReactionToRouteResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteAReactionToRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteAReactionToRouteResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully delete a reaction to a route.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionRepository).findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any());
        verify(reactionRepository).delete(Mockito.<ReactionEntity>any());
        verify(routeRepository).save(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(routeEntity).getId();
        verify(routeEntity).getReactions();
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link ReactionController#deleteAReactionToRoute(Long, UserPrincipal)}
     */
    @Test
    public void testDeleteAReactionToRoute4() {

        ReactionService reactionService = mock(ReactionService.class);
        doNothing().when(reactionService)
                .deleteReaction(Mockito.<Long>any(), Mockito.<UserPrincipal>any(), Mockito.<ReactionTargetType>any());
        ReactionController reactionController = new ReactionController(reactionService);
        ResponseEntity<ResponseDTO<Object>> actualDeleteAReactionToRouteResult = reactionController
                .deleteAReactionToRoute(1L, new UserPrincipal());
        assertTrue(actualDeleteAReactionToRouteResult.hasBody());
        assertTrue(actualDeleteAReactionToRouteResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteAReactionToRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteAReactionToRouteResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully delete a reaction to a route.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionService).deleteReaction(Mockito.<Long>any(), Mockito.<UserPrincipal>any(),
                Mockito.<ReactionTargetType>any());
    }

    /**
     * Method under test: {@link ReactionController#reactToComment(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testReactToComment() {

        ReactionController reactionController = new ReactionController(new ReactionService(mock(ReactionRepository.class),
                mock(CommentRepository.class), mock(RouteRepository.class), mock(UserRepository.class)));
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.ROUTE);

        ResponseEntity<ResponseDTO<Object>> actualReactToCommentResult = reactionController.reactToComment(1L,
                userPrincipal, reactionCreationDTO, new BindException("Target", "Object Name"));
        assertTrue(actualReactToCommentResult.hasBody());
        assertTrue(actualReactToCommentResult.getHeaders().isEmpty());
        assertEquals(400, actualReactToCommentResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualReactToCommentResult.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("", body.getMessage());
        assertNull(body.getContent());
    }

    /**
     * Method under test: {@link ReactionController#reactToComment(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testReactToComment2() {

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

        ReactionEntity reactionEntity = new ReactionEntity();
        reactionEntity.setAuthor(author);
        reactionEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity.setId(1L);
        reactionEntity.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity.setReactionType(ReactionEnum.LIKE);
        reactionEntity.setTargetEntityId(1L);
        reactionEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        when(reactionRepository.save(Mockito.<ReactionEntity>any())).thenReturn(reactionEntity);
        CommentEntity commentEntity = mock(CommentEntity.class);
        when(commentEntity.getId()).thenReturn(1L);
        when(commentEntity.getReactions()).thenReturn(new HashSet<>());
        Optional<CommentEntity> ofResult = Optional.of(commentEntity);

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

        PictureEntity profilePicture3 = new PictureEntity();
        profilePicture3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setId(1L);
        profilePicture3.setPublicId("42");
        profilePicture3.setTitle("Dr");
        profilePicture3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setUrl("https://example.org/example");

        UserEntity author3 = new UserEntity();
        author3.setAccountExpired(true);
        author3.setBirthdate(LocalDate.of(1970, 1, 1));
        author3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author3.setCredentialsExpired(true);
        author3.setEmail("jane.doe@example.org");
        author3.setEnabled(true);
        author3.setFirstName("Jane");
        author3.setId(1L);
        author3.setLastName("Doe");
        author3.setLocked(true);
        author3.setPassword("iloveyou");
        author3.setProfilePicture(profilePicture3);
        author3.setRoles(new HashSet<>());
        author3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author3.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity route = new RouteEntity();
        route.setAuthor(author3);
        route.setCategories(new HashSet<>());
        route.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        route.setDescription("The characteristics of someone or something");
        route.setGpxCoordinates("Gpx Coordinates");
        route.setId(1L);
        route.setLevelType(LevelEnum.BEGINNER);
        route.setMainPicture(mainPicture);
        route.setName("Name");
        route.setPictures(new HashSet<>());
        route.setReactions(new HashSet<>());
        route.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        route.setVideoUrl("https://example.org/example");

        CommentEntity commentEntity2 = new CommentEntity();
        commentEntity2.setAuthor(author2);
        commentEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        commentEntity2.setId(1L);
        commentEntity2.setReactions(new HashSet<>());
        commentEntity2.setRoute(route);
        commentEntity2.setTextContent("Not all who wander are lost");
        commentEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        CommentRepository commentRepository = mock(CommentRepository.class);
        when(commentRepository.save(Mockito.<CommentEntity>any())).thenReturn(commentEntity2);
        when(commentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        PictureEntity profilePicture4 = new PictureEntity();
        profilePicture4.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture4.setId(1L);
        profilePicture4.setPublicId("42");
        profilePicture4.setTitle("Dr");
        profilePicture4.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture4.setUrl("https://example.org/example");

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
        userEntity.setProfilePicture(profilePicture4);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        ReactionController reactionController = new ReactionController(
                new ReactionService(reactionRepository, commentRepository, mock(RouteRepository.class), userRepository));
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.COMMENT);

        ResponseEntity<ResponseDTO<Object>> actualReactToCommentResult = reactionController.reactToComment(1L,
                userPrincipal, reactionCreationDTO, new BindException("Target", "Object Name"));
        assertTrue(actualReactToCommentResult.hasBody());
        assertTrue(actualReactToCommentResult.getHeaders().isEmpty());
        assertEquals(200, actualReactToCommentResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualReactToCommentResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully react to a comment.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionRepository).save(Mockito.<ReactionEntity>any());
        verify(commentRepository).save(Mockito.<CommentEntity>any());
        verify(commentRepository).findById(Mockito.<Long>any());
        verify(commentEntity).getId();
        verify(commentEntity).getReactions();
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link ReactionController#editReactionToComment(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testEditReactionToComment() {

        ReactionController reactionController = new ReactionController(new ReactionService(mock(ReactionRepository.class),
                mock(CommentRepository.class), mock(RouteRepository.class), mock(UserRepository.class)));
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.ROUTE);

        ResponseEntity<ResponseDTO<Object>> actualEditReactionToCommentResult = reactionController
                .editReactionToComment(1L, userPrincipal, reactionCreationDTO, new BindException("Target", "Object Name"));
        assertTrue(actualEditReactionToCommentResult.hasBody());
        assertTrue(actualEditReactionToCommentResult.getHeaders().isEmpty());
        assertEquals(400, actualEditReactionToCommentResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualEditReactionToCommentResult.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("", body.getMessage());
        assertNull(body.getContent());
    }

    /**
     * Method under test: {@link ReactionController#editReactionToComment(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testEditReactionToComment2() {

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

        ReactionEntity reactionEntity = new ReactionEntity();
        reactionEntity.setAuthor(author);
        reactionEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity.setId(1L);
        reactionEntity.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity.setReactionType(ReactionEnum.LIKE);
        reactionEntity.setTargetEntityId(1L);
        reactionEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ReactionEntity> ofResult = Optional.of(reactionEntity);

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

        ReactionEntity reactionEntity2 = new ReactionEntity();
        reactionEntity2.setAuthor(author2);
        reactionEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity2.setId(1L);
        reactionEntity2.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity2.setReactionType(ReactionEnum.LIKE);
        reactionEntity2.setTargetEntityId(1L);
        reactionEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        when(reactionRepository.save(Mockito.<ReactionEntity>any())).thenReturn(reactionEntity2);
        when(reactionRepository.findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        CommentEntity commentEntity = mock(CommentEntity.class);
        when(commentEntity.getId()).thenReturn(1L);
        CommentRepository commentRepository = mock(CommentRepository.class);
        when(commentRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(commentEntity));

        PictureEntity profilePicture3 = new PictureEntity();
        profilePicture3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setId(1L);
        profilePicture3.setPublicId("42");
        profilePicture3.setTitle("Dr");
        profilePicture3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setUrl("https://example.org/example");

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
        userEntity.setProfilePicture(profilePicture3);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        ReactionController reactionController = new ReactionController(
                new ReactionService(reactionRepository, commentRepository, mock(RouteRepository.class), userRepository));
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.COMMENT);

        ResponseEntity<ResponseDTO<Object>> actualEditReactionToCommentResult = reactionController
                .editReactionToComment(1L, userPrincipal, reactionCreationDTO, new BindException("Target", "Object Name"));
        assertTrue(actualEditReactionToCommentResult.hasBody());
        assertTrue(actualEditReactionToCommentResult.getHeaders().isEmpty());
        assertEquals(200, actualEditReactionToCommentResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualEditReactionToCommentResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully edit reaction to a comment.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionRepository).save(Mockito.<ReactionEntity>any());
        verify(reactionRepository).findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any());
        verify(commentRepository).findById(Mockito.<Long>any());
        verify(commentEntity).getId();
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link ReactionController#editReactionToComment(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testEditReactionToComment3() {

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
        ReactionEntity reactionEntity = mock(ReactionEntity.class);
        doNothing().when(reactionEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(reactionEntity).setId(Mockito.<Long>any());
        doNothing().when(reactionEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(reactionEntity).setAuthor(Mockito.<UserEntity>any());
        doNothing().when(reactionEntity).setReactionTargetType(Mockito.<ReactionTargetType>any());
        doNothing().when(reactionEntity).setReactionType(Mockito.<ReactionEnum>any());
        doNothing().when(reactionEntity).setTargetEntityId(Mockito.<Long>any());
        reactionEntity.setAuthor(author);
        reactionEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity.setId(1L);
        reactionEntity.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity.setReactionType(ReactionEnum.LIKE);
        reactionEntity.setTargetEntityId(1L);
        reactionEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ReactionEntity> ofResult = Optional.of(reactionEntity);

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

        ReactionEntity reactionEntity2 = new ReactionEntity();
        reactionEntity2.setAuthor(author2);
        reactionEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity2.setId(1L);
        reactionEntity2.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity2.setReactionType(ReactionEnum.LIKE);
        reactionEntity2.setTargetEntityId(1L);
        reactionEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        when(reactionRepository.save(Mockito.<ReactionEntity>any())).thenReturn(reactionEntity2);
        when(reactionRepository.findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        CommentEntity commentEntity = mock(CommentEntity.class);
        when(commentEntity.getId()).thenReturn(1L);
        CommentRepository commentRepository = mock(CommentRepository.class);
        when(commentRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(commentEntity));

        PictureEntity profilePicture3 = new PictureEntity();
        profilePicture3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setId(1L);
        profilePicture3.setPublicId("42");
        profilePicture3.setTitle("Dr");
        profilePicture3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setUrl("https://example.org/example");

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
        userEntity.setProfilePicture(profilePicture3);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        ReactionController reactionController = new ReactionController(
                new ReactionService(reactionRepository, commentRepository, mock(RouteRepository.class), userRepository));
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.COMMENT);

        ResponseEntity<ResponseDTO<Object>> actualEditReactionToCommentResult = reactionController
                .editReactionToComment(1L, userPrincipal, reactionCreationDTO, new BindException("Target", "Object Name"));
        assertTrue(actualEditReactionToCommentResult.hasBody());
        assertTrue(actualEditReactionToCommentResult.getHeaders().isEmpty());
        assertEquals(200, actualEditReactionToCommentResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualEditReactionToCommentResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully edit reaction to a comment.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionRepository).save(Mockito.<ReactionEntity>any());
        verify(reactionRepository).findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any());
        verify(reactionEntity).setCreatedOn(Mockito.<LocalDateTime>any());
        verify(reactionEntity).setId(Mockito.<Long>any());
        verify(reactionEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        verify(reactionEntity).setAuthor(Mockito.<UserEntity>any());
        verify(reactionEntity).setReactionTargetType(Mockito.<ReactionTargetType>any());
        verify(reactionEntity, atLeast(1)).setReactionType(Mockito.<ReactionEnum>any());
        verify(reactionEntity).setTargetEntityId(Mockito.<Long>any());
        verify(commentRepository).findById(Mockito.<Long>any());
        verify(commentEntity).getId();
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link ReactionController#editReactionToComment(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testEditReactionToComment4() {

        ReactionService reactionService = mock(ReactionService.class);
        doNothing().when(reactionService)
                .editReaction(Mockito.<Long>any(), Mockito.<UserPrincipal>any(), Mockito.<ReactionCreationDTO>any());
        ReactionController reactionController = new ReactionController(reactionService);
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.COMMENT);

        ResponseEntity<ResponseDTO<Object>> actualEditReactionToCommentResult = reactionController
                .editReactionToComment(1L, userPrincipal, reactionCreationDTO, new BindException("Target", "Object Name"));
        assertTrue(actualEditReactionToCommentResult.hasBody());
        assertTrue(actualEditReactionToCommentResult.getHeaders().isEmpty());
        assertEquals(200, actualEditReactionToCommentResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualEditReactionToCommentResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully edit reaction to a comment.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionService).editReaction(Mockito.<Long>any(), Mockito.<UserPrincipal>any(),
                Mockito.<ReactionCreationDTO>any());
    }

    /**
     * Method under test: {@link ReactionController#editReactionToComment(Long, UserPrincipal, ReactionCreationDTO, BindingResult)}
     */
    @Test
    public void testEditReactionToComment5() {

        ReactionController reactionController = new ReactionController(mock(ReactionService.class));
        UserPrincipal userPrincipal = new UserPrincipal();
        ReactionCreationDTO reactionCreationDTO = new ReactionCreationDTO(ReactionEnum.LIKE, ReactionTargetType.COMMENT);

        BeanPropertyBindingResult bindingResult = mock(BeanPropertyBindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());
        when(bindingResult.hasErrors()).thenReturn(true);
        ResponseEntity<ResponseDTO<Object>> actualEditReactionToCommentResult = reactionController
                .editReactionToComment(1L, userPrincipal, reactionCreationDTO, bindingResult);
        assertTrue(actualEditReactionToCommentResult.hasBody());
        assertTrue(actualEditReactionToCommentResult.getHeaders().isEmpty());
        assertEquals(400, actualEditReactionToCommentResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualEditReactionToCommentResult.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("", body.getMessage());
        assertNull(body.getContent());
        verify(bindingResult).hasErrors();
        verify(bindingResult).getAllErrors();
    }

    /**
     * Method under test: {@link ReactionController#deleteAReactionToComment(Long, UserPrincipal)}
     */
    @Test
    public void testDeleteAReactionToComment() {

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

        ReactionEntity reactionEntity = new ReactionEntity();
        reactionEntity.setAuthor(author);
        reactionEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        reactionEntity.setId(1L);
        reactionEntity.setReactionTargetType(ReactionTargetType.ROUTE);
        reactionEntity.setReactionType(ReactionEnum.LIKE);
        reactionEntity.setTargetEntityId(1L);
        reactionEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ReactionEntity> ofResult = Optional.of(reactionEntity);
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        doNothing().when(reactionRepository).delete(Mockito.<ReactionEntity>any());
        when(reactionRepository.findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any()))
                .thenReturn(ofResult);
        CommentEntity commentEntity = mock(CommentEntity.class);
        when(commentEntity.getId()).thenReturn(1L);
        when(commentEntity.getReactions()).thenReturn(new HashSet<>());
        Optional<CommentEntity> ofResult2 = Optional.of(commentEntity);

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

        PictureEntity profilePicture3 = new PictureEntity();
        profilePicture3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setId(1L);
        profilePicture3.setPublicId("42");
        profilePicture3.setTitle("Dr");
        profilePicture3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setUrl("https://example.org/example");

        UserEntity author3 = new UserEntity();
        author3.setAccountExpired(true);
        author3.setBirthdate(LocalDate.of(1970, 1, 1));
        author3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author3.setCredentialsExpired(true);
        author3.setEmail("jane.doe@example.org");
        author3.setEnabled(true);
        author3.setFirstName("Jane");
        author3.setId(1L);
        author3.setLastName("Doe");
        author3.setLocked(true);
        author3.setPassword("iloveyou");
        author3.setProfilePicture(profilePicture3);
        author3.setRoles(new HashSet<>());
        author3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author3.setUsername("janedoe");

        PictureEntity mainPicture = new PictureEntity();
        mainPicture.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setId(1L);
        mainPicture.setPublicId("42");
        mainPicture.setTitle("Dr");
        mainPicture.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture.setUrl("https://example.org/example");

        RouteEntity route = new RouteEntity();
        route.setAuthor(author3);
        route.setCategories(new HashSet<>());
        route.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        route.setDescription("The characteristics of someone or something");
        route.setGpxCoordinates("Gpx Coordinates");
        route.setId(1L);
        route.setLevelType(LevelEnum.BEGINNER);
        route.setMainPicture(mainPicture);
        route.setName("Name");
        route.setPictures(new HashSet<>());
        route.setReactions(new HashSet<>());
        route.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        route.setVideoUrl("https://example.org/example");

        CommentEntity commentEntity2 = new CommentEntity();
        commentEntity2.setAuthor(author2);
        commentEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        commentEntity2.setId(1L);
        commentEntity2.setReactions(new HashSet<>());
        commentEntity2.setRoute(route);
        commentEntity2.setTextContent("Not all who wander are lost");
        commentEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        CommentRepository commentRepository = mock(CommentRepository.class);
        when(commentRepository.save(Mockito.<CommentEntity>any())).thenReturn(commentEntity2);
        when(commentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        PictureEntity profilePicture4 = new PictureEntity();
        profilePicture4.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture4.setId(1L);
        profilePicture4.setPublicId("42");
        profilePicture4.setTitle("Dr");
        profilePicture4.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture4.setUrl("https://example.org/example");

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
        userEntity.setProfilePicture(profilePicture4);
        userEntity.setRoles(new HashSet<>());
        userEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        ReactionController reactionController = new ReactionController(
                new ReactionService(reactionRepository, commentRepository, mock(RouteRepository.class), userRepository));
        ResponseEntity<ResponseDTO<Object>> actualDeleteAReactionToCommentResult = reactionController
                .deleteAReactionToComment(1L, new UserPrincipal());
        assertTrue(actualDeleteAReactionToCommentResult.hasBody());
        assertTrue(actualDeleteAReactionToCommentResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteAReactionToCommentResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteAReactionToCommentResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully delete a reaction to a comment.", body.getMessage());
        assertNull(body.getContent());
        verify(reactionRepository).findByAuthorAndTargetEntityId(Mockito.<UserEntity>any(), Mockito.<Long>any());
        verify(reactionRepository).delete(Mockito.<ReactionEntity>any());
        verify(commentRepository).save(Mockito.<CommentEntity>any());
        verify(commentRepository).findById(Mockito.<Long>any());
        verify(commentEntity).getId();
        verify(commentEntity).getReactions();
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }
}

