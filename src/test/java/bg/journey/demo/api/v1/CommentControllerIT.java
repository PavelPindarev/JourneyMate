package bg.journey.demo.api.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bg.journey.demo.dto.payload.CreationCommentDTO;
import bg.journey.demo.dto.response.CommentDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.model.entity.CommentEntity;
import bg.journey.demo.model.entity.PictureEntity;
import bg.journey.demo.model.entity.ReactionEntity;
import bg.journey.demo.model.entity.RouteEntity;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.model.enums.LevelEnum;
import bg.journey.demo.model.mapper.CommentMapper;
import bg.journey.demo.model.mapper.CommentMapperImpl;
import bg.journey.demo.repository.CommentRepository;
import bg.journey.demo.repository.ReactionRepository;
import bg.journey.demo.repository.RouteRepository;
import bg.journey.demo.repository.UserRepository;
import bg.journey.demo.security.UserPrincipal;
import bg.journey.demo.service.CommentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class CommentControllerIT {
    /**
     * Method under test: {@link CommentController#getAllCommentsForARoute(Long)}
     */
    @Test
    public void testGetAllCommentsForARoute() {

        CommentRepository commentRepository = mock(CommentRepository.class);
        ArrayList<CommentEntity> commentEntityList = new ArrayList<>();
        when(commentRepository.findAllByRoute(Mockito.<RouteEntity>any())).thenReturn(commentEntityList);
        RouteRepository routeRepository = mock(RouteRepository.class);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(mock(RouteEntity.class)));
        ResponseEntity<ResponseDTO<List<CommentDTO>>> actualAllCommentsForARoute = (new CommentController(
                new CommentService(commentRepository, routeRepository, new CommentMapperImpl(), mock(UserRepository.class),
                        mock(ReactionRepository.class)))).getAllCommentsForARoute(1L);
        assertTrue(actualAllCommentsForARoute.hasBody());
        assertTrue(actualAllCommentsForARoute.getHeaders().isEmpty());
        assertEquals(200, actualAllCommentsForARoute.getStatusCodeValue());
        ResponseDTO<List<CommentDTO>> body = actualAllCommentsForARoute.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get all comments for a route!", body.getMessage());
        assertEquals(commentEntityList, body.getContent());
        verify(commentRepository).findAllByRoute(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CommentController#getAllCommentsForARoute(Long)}
     */
    @Test
    public void testGetAllCommentsForARoute2() {

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

        RouteEntity route = new RouteEntity();
        route.setAuthor(author2);
        route.setCategories(new HashSet<>());
        route.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        route.setDescription("The characteristics of someone or something");
        route.setGpxCoordinates("Successfully get all comments for a route!");
        route.setId(1L);
        route.setLevelType(LevelEnum.BEGINNER);
        route.setMainPicture(mainPicture);
        route.setName("Successfully get all comments for a route!");
        route.setPictures(new HashSet<>());
        route.setReactions(new HashSet<>());
        route.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        route.setVideoUrl("https://example.org/example");

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setAuthor(author);
        commentEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        commentEntity.setId(1L);
        commentEntity.setReactions(new HashSet<>());
        commentEntity.setRoute(route);
        commentEntity.setTextContent("Not all who wander are lost");
        commentEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<CommentEntity> commentEntityList = new ArrayList<>();
        commentEntityList.add(commentEntity);
        CommentRepository commentRepository = mock(CommentRepository.class);
        when(commentRepository.findAllByRoute(Mockito.<RouteEntity>any())).thenReturn(commentEntityList);
        RouteRepository routeRepository = mock(RouteRepository.class);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(mock(RouteEntity.class)));
        ResponseEntity<ResponseDTO<List<CommentDTO>>> actualAllCommentsForARoute = (new CommentController(
                new CommentService(commentRepository, routeRepository, new CommentMapperImpl(), mock(UserRepository.class),
                        mock(ReactionRepository.class)))).getAllCommentsForARoute(1L);
        assertTrue(actualAllCommentsForARoute.hasBody());
        assertTrue(actualAllCommentsForARoute.getHeaders().isEmpty());
        assertEquals(200, actualAllCommentsForARoute.getStatusCodeValue());
        ResponseDTO<List<CommentDTO>> body = actualAllCommentsForARoute.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get all comments for a route!", body.getMessage());
        assertEquals(1, body.getContent().size());
        verify(commentRepository).findAllByRoute(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CommentController#getAllCommentsForARoute(Long)}
     */
    @Test
    public void testGetAllCommentsForARoute3() {

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

        RouteEntity route = new RouteEntity();
        route.setAuthor(author2);
        route.setCategories(new HashSet<>());
        route.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        route.setDescription("The characteristics of someone or something");
        route.setGpxCoordinates("Successfully get all comments for a route!");
        route.setId(1L);
        route.setLevelType(LevelEnum.BEGINNER);
        route.setMainPicture(mainPicture);
        route.setName("Successfully get all comments for a route!");
        route.setPictures(new HashSet<>());
        route.setReactions(new HashSet<>());
        route.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        route.setVideoUrl("https://example.org/example");

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setAuthor(author);
        commentEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        commentEntity.setId(1L);
        commentEntity.setReactions(new HashSet<>());
        commentEntity.setRoute(route);
        commentEntity.setTextContent("Not all who wander are lost");
        commentEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());

        PictureEntity profilePicture3 = new PictureEntity();
        profilePicture3.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setId(2L);
        profilePicture3.setPublicId("Successfully get all comments for a route!");
        profilePicture3.setTitle("Mr");
        profilePicture3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture3.setUrl("Successfully get all comments for a route!");

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
        author3.setPassword("Successfully get all comments for a route!");
        author3.setProfilePicture(profilePicture3);
        author3.setRoles(new HashSet<>());
        author3.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author3.setUsername("Successfully get all comments for a route!");

        PictureEntity profilePicture4 = new PictureEntity();
        profilePicture4.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture4.setId(2L);
        profilePicture4.setPublicId("Successfully get all comments for a route!");
        profilePicture4.setTitle("Mr");
        profilePicture4.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        profilePicture4.setUrl("Successfully get all comments for a route!");

        UserEntity author4 = new UserEntity();
        author4.setAccountExpired(false);
        author4.setBirthdate(LocalDate.of(1970, 1, 1));
        author4.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author4.setCredentialsExpired(false);
        author4.setEmail("john.smith@example.org");
        author4.setEnabled(false);
        author4.setFirstName("John");
        author4.setId(2L);
        author4.setLastName("Smith");
        author4.setLocked(false);
        author4.setPassword("Successfully get all comments for a route!");
        author4.setProfilePicture(profilePicture4);
        author4.setRoles(new HashSet<>());
        author4.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        author4.setUsername("Successfully get all comments for a route!");

        PictureEntity mainPicture2 = new PictureEntity();
        mainPicture2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setId(2L);
        mainPicture2.setPublicId("Successfully get all comments for a route!");
        mainPicture2.setTitle("Mr");
        mainPicture2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        mainPicture2.setUrl("Successfully get all comments for a route!");

        RouteEntity route2 = new RouteEntity();
        route2.setAuthor(author4);
        route2.setCategories(new HashSet<>());
        route2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        route2.setDescription("Successfully get all comments for a route!");
        route2.setGpxCoordinates("Gpx Coordinates");
        route2.setId(2L);
        route2.setLevelType(LevelEnum.INTERMEDIATE);
        route2.setMainPicture(mainPicture2);
        route2.setName("Name");
        route2.setPictures(new HashSet<>());
        route2.setReactions(new HashSet<>());
        route2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        route2.setVideoUrl("Successfully get all comments for a route!");

        CommentEntity commentEntity2 = new CommentEntity();
        commentEntity2.setAuthor(author3);
        commentEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        commentEntity2.setId(2L);
        commentEntity2.setReactions(new HashSet<>());
        commentEntity2.setRoute(route2);
        commentEntity2.setTextContent("Successfully get all comments for a route!");
        commentEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<CommentEntity> commentEntityList = new ArrayList<>();
        commentEntityList.add(commentEntity2);
        commentEntityList.add(commentEntity);
        CommentRepository commentRepository = mock(CommentRepository.class);
        when(commentRepository.findAllByRoute(Mockito.<RouteEntity>any())).thenReturn(commentEntityList);
        RouteRepository routeRepository = mock(RouteRepository.class);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(mock(RouteEntity.class)));
        ResponseEntity<ResponseDTO<List<CommentDTO>>> actualAllCommentsForARoute = (new CommentController(
                new CommentService(commentRepository, routeRepository, new CommentMapperImpl(), mock(UserRepository.class),
                        mock(ReactionRepository.class)))).getAllCommentsForARoute(1L);
        assertTrue(actualAllCommentsForARoute.hasBody());
        assertTrue(actualAllCommentsForARoute.getHeaders().isEmpty());
        assertEquals(200, actualAllCommentsForARoute.getStatusCodeValue());
        ResponseDTO<List<CommentDTO>> body = actualAllCommentsForARoute.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get all comments for a route!", body.getMessage());
        assertEquals(2, body.getContent().size());
        verify(commentRepository).findAllByRoute(Mockito.<RouteEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CommentController#getAllCommentsForARoute(Long)}
     */
    @Test
    public void testGetAllCommentsForARoute4() {

        CommentService commentService = mock(CommentService.class);
        when(commentService.getAllCommentsForRoute(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ResponseEntity<ResponseDTO<List<CommentDTO>>> actualAllCommentsForARoute = (new CommentController(commentService))
                .getAllCommentsForARoute(1L);
        assertTrue(actualAllCommentsForARoute.hasBody());
        assertTrue(actualAllCommentsForARoute.getHeaders().isEmpty());
        assertEquals(200, actualAllCommentsForARoute.getStatusCodeValue());
        ResponseDTO<List<CommentDTO>> body = actualAllCommentsForARoute.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get all comments for a route!", body.getMessage());
        assertTrue(body.getContent().isEmpty());
        verify(commentService).getAllCommentsForRoute(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CommentController#getCommentById(Long)}
     */
    @Test
    public void testGetCommentById() {

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
        CommentEntity commentEntity = mock(CommentEntity.class);
        when(commentEntity.getTextContent()).thenReturn("Not all who wander are lost");
        when(commentEntity.getCreatedOn()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(commentEntity.getUpdatedOn()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(commentEntity.getReactions()).thenReturn(new HashSet<>());
        when(commentEntity.getAuthor()).thenReturn(userEntity);
        when(commentEntity.getId()).thenReturn(1L);
        CommentRepository commentRepository = mock(CommentRepository.class);
        when(commentRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(commentEntity));
        RouteRepository routeRepository = mock(RouteRepository.class);
        ResponseEntity<ResponseDTO<CommentDTO>> actualCommentById = (new CommentController(
                new CommentService(commentRepository, routeRepository, new CommentMapperImpl(), mock(UserRepository.class),
                        mock(ReactionRepository.class)))).getCommentById(1L);
        assertTrue(actualCommentById.hasBody());
        assertTrue(actualCommentById.getHeaders().isEmpty());
        assertEquals(200, actualCommentById.getStatusCodeValue());
        ResponseDTO<CommentDTO> body = actualCommentById.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get a comment by id!", body.getMessage());
        CommentDTO content = body.getContent();
        assertEquals("janedoe", content.getAuthorUsername());
        assertEquals("Not all who wander are lost", content.getTextContent());
        assertEquals("00:00", content.getUpdatedOn().toLocalTime().toString());
        assertTrue(content.getReactions().isEmpty());
        assertEquals(1L, content.getId().longValue());
        assertEquals("1970-01-01", content.getCreatedOn().toLocalDate().toString());
        verify(commentRepository).findById(Mockito.<Long>any());
        verify(commentEntity).getAuthor();
        verify(commentEntity).getId();
        verify(commentEntity).getTextContent();
        verify(commentEntity).getCreatedOn();
        verify(commentEntity).getUpdatedOn();
        verify(commentEntity).getReactions();
    }

    /**
     * Method under test: {@link CommentController#getCommentById(Long)}
     */
    @Test
    public void testGetCommentById2() {

        CommentRepository commentRepository = mock(CommentRepository.class);
        when(commentRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(mock(CommentEntity.class)));
        CommentMapper commentMapper = mock(CommentMapper.class);
        CommentDTO commentDTO = new CommentDTO();
        when(commentMapper.commentEntityToCommentDTO(Mockito.<CommentEntity>any())).thenReturn(commentDTO);
        ResponseEntity<ResponseDTO<CommentDTO>> actualCommentById = (new CommentController(
                new CommentService(commentRepository, mock(RouteRepository.class), commentMapper, mock(UserRepository.class),
                        mock(ReactionRepository.class)))).getCommentById(1L);
        assertTrue(actualCommentById.hasBody());
        assertTrue(actualCommentById.getHeaders().isEmpty());
        assertEquals(200, actualCommentById.getStatusCodeValue());
        ResponseDTO<CommentDTO> body = actualCommentById.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get a comment by id!", body.getMessage());
        assertSame(commentDTO, body.getContent());
        verify(commentRepository).findById(Mockito.<Long>any());
        verify(commentMapper).commentEntityToCommentDTO(Mockito.<CommentEntity>any());
    }

    /**
     * Method under test: {@link CommentController#getCommentById(Long)}
     */
    @Test
    public void testGetCommentById3() {

        CommentService commentService = mock(CommentService.class);
        CommentDTO commentDTO = new CommentDTO();
        when(commentService.getCommentById(Mockito.<Long>any())).thenReturn(commentDTO);
        ResponseEntity<ResponseDTO<CommentDTO>> actualCommentById = (new CommentController(commentService))
                .getCommentById(1L);
        assertTrue(actualCommentById.hasBody());
        assertTrue(actualCommentById.getHeaders().isEmpty());
        assertEquals(200, actualCommentById.getStatusCodeValue());
        ResponseDTO<CommentDTO> body = actualCommentById.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully get a comment by id!", body.getMessage());
        assertSame(commentDTO, body.getContent());
        verify(commentService).getCommentById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CommentController#createACommentForRoute(Long, UserPrincipal, CreationCommentDTO, BindingResult)}
     */
    @Test
    public void testCreateACommentForRoute() {

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

        RouteEntity route = new RouteEntity();
        route.setAuthor(author2);
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

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setAuthor(author);
        commentEntity.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        commentEntity.setId(1L);
        commentEntity.setReactions(new HashSet<>());
        commentEntity.setRoute(route);
        commentEntity.setTextContent("Not all who wander are lost");
        commentEntity.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        CommentRepository commentRepository = mock(CommentRepository.class);
        when(commentRepository.save(Mockito.<CommentEntity>any())).thenReturn(commentEntity);
        RouteRepository routeRepository = mock(RouteRepository.class);
        when(routeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(mock(RouteEntity.class)));

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
        CommentController commentController = new CommentController(new CommentService(commentRepository, routeRepository,
                new CommentMapperImpl(), userRepository, mock(ReactionRepository.class)));
        UserPrincipal userPrincipal = new UserPrincipal();
        CreationCommentDTO creationCommentDTO = new CreationCommentDTO("Text Context");
        ResponseEntity<ResponseDTO<Object>> actualCreateACommentForRouteResult = commentController
                .createACommentForRoute(1L, userPrincipal, creationCommentDTO, new BindException("Target", "Object Name"));
        assertTrue(actualCreateACommentForRouteResult.hasBody());
        assertTrue(actualCreateACommentForRouteResult.getHeaders().isEmpty());
        assertEquals(200, actualCreateACommentForRouteResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualCreateACommentForRouteResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully create a comment comment!", body.getMessage());
        assertNull(body.getContent());
        verify(commentRepository).save(Mockito.<CommentEntity>any());
        verify(routeRepository).findById(Mockito.<Long>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link CommentController#editComment(Long, UserPrincipal, CreationCommentDTO, BindingResult)}
     */
    @Test
    public void testEditComment() {

        CommentEntity commentEntity = mock(CommentEntity.class);
        doNothing().when(commentEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        doNothing().when(commentEntity).setTextContent(Mockito.<String>any());
        Optional<CommentEntity> ofResult = Optional.of(commentEntity);

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

        RouteEntity route = new RouteEntity();
        route.setAuthor(author2);
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
        commentEntity2.setAuthor(author);
        commentEntity2.setCreatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        commentEntity2.setId(1L);
        commentEntity2.setReactions(new HashSet<>());
        commentEntity2.setRoute(route);
        commentEntity2.setTextContent("Not all who wander are lost");
        commentEntity2.setUpdatedOn(LocalDate.of(1970, 1, 1).atStartOfDay());
        CommentRepository commentRepository = mock(CommentRepository.class);
        when(commentRepository.save(Mockito.<CommentEntity>any())).thenReturn(commentEntity2);
        when(commentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        RouteRepository routeRepository = mock(RouteRepository.class);
        CommentController commentController = new CommentController(new CommentService(commentRepository, routeRepository,
                new CommentMapperImpl(), mock(UserRepository.class), mock(ReactionRepository.class)));
        UserPrincipal userPrincipal = new UserPrincipal();
        CreationCommentDTO creationCommentDTO = new CreationCommentDTO("Text Context");
        ResponseEntity<ResponseDTO<Object>> actualEditCommentResult = commentController.editComment(1L, userPrincipal,
                creationCommentDTO, new BindException("Target", "Object Name"));
        assertTrue(actualEditCommentResult.hasBody());
        assertTrue(actualEditCommentResult.getHeaders().isEmpty());
        assertEquals(200, actualEditCommentResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualEditCommentResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully edit a comment!", body.getMessage());
        assertNull(body.getContent());
        verify(commentRepository).save(Mockito.<CommentEntity>any());
        verify(commentRepository).findById(Mockito.<Long>any());
        verify(commentEntity).setUpdatedOn(Mockito.<LocalDateTime>any());
        verify(commentEntity).setTextContent(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CommentController#deleteComment(Long, UserPrincipal)}
     */
    @Test
    public void testDeleteComment() {

        CommentEntity commentEntity = mock(CommentEntity.class);
        when(commentEntity.getReactions()).thenReturn(new HashSet<>());
        doNothing().when(commentEntity).setAuthor(Mockito.<UserEntity>any());
        doNothing().when(commentEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        doNothing().when(commentEntity).setRoute(Mockito.<RouteEntity>any());
        Optional<CommentEntity> ofResult = Optional.of(commentEntity);
        CommentRepository commentRepository = mock(CommentRepository.class);
        doNothing().when(commentRepository).delete(Mockito.<CommentEntity>any());
        when(commentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ReactionRepository reactionRepository = mock(ReactionRepository.class);
        doNothing().when(reactionRepository).deleteAll(Mockito.<Iterable<ReactionEntity>>any());
        RouteRepository routeRepository = mock(RouteRepository.class);
        CommentController commentController = new CommentController(new CommentService(commentRepository, routeRepository,
                new CommentMapperImpl(), mock(UserRepository.class), reactionRepository));
        ResponseEntity<ResponseDTO<Object>> actualDeleteCommentResult = commentController.deleteComment(1L,
                new UserPrincipal());
        assertTrue(actualDeleteCommentResult.hasBody());
        assertTrue(actualDeleteCommentResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteCommentResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteCommentResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully delete a comment!", body.getMessage());
        assertNull(body.getContent());
        verify(commentRepository).findById(Mockito.<Long>any());
        verify(commentRepository).delete(Mockito.<CommentEntity>any());
        verify(commentEntity).getReactions();
        verify(commentEntity).setAuthor(Mockito.<UserEntity>any());
        verify(commentEntity).setReactions(Mockito.<Set<ReactionEntity>>any());
        verify(commentEntity).setRoute(Mockito.<RouteEntity>any());
        verify(reactionRepository).deleteAll(Mockito.<Iterable<ReactionEntity>>any());
    }

    /**
     * Method under test: {@link CommentController#deleteComment(Long, UserPrincipal)}
     */
    @Test
    public void testDeleteComment2() {

        CommentService commentService = mock(CommentService.class);
        doNothing().when(commentService).deleteComment(Mockito.<Long>any());
        CommentController commentController = new CommentController(commentService);
        ResponseEntity<ResponseDTO<Object>> actualDeleteCommentResult = commentController.deleteComment(1L,
                new UserPrincipal());
        assertTrue(actualDeleteCommentResult.hasBody());
        assertTrue(actualDeleteCommentResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteCommentResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualDeleteCommentResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Successfully delete a comment!", body.getMessage());
        assertNull(body.getContent());
        verify(commentService).deleteComment(Mockito.<Long>any());
    }
}

