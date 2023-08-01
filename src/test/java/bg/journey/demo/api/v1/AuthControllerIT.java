package bg.journey.demo.api.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bg.journey.demo.dto.payload.SigninDTO;
import bg.journey.demo.dto.payload.SingupDTO;
import bg.journey.demo.dto.response.JwtResponseDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.exception.InvalidCredentialsException;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.repository.RoleRepository;
import bg.journey.demo.repository.UserRepository;
import bg.journey.demo.security.JwtTokenProvider;
import bg.journey.demo.service.AuthService;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class AuthControllerIT {
    /**
     * Method under test: {@link AuthController#signIn(SigninDTO)}
     */
    @Test
    public void testSignIn() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(new UserEntity()));
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(new AuthService(authenticationManager, jwtTokenProvider,
                userRepository, passwordEncoder, new ModelMapper(), mock(RoleRepository.class)));
        ResponseEntity<ResponseDTO<JwtResponseDTO>> actualSignInResult = authController
                .signIn(new SigninDTO("janedoe", "iloveyou"));
        assertTrue(actualSignInResult.hasBody());
        assertTrue(actualSignInResult.getHeaders().isEmpty());
        assertEquals(401, actualSignInResult.getStatusCodeValue());
        ResponseDTO<JwtResponseDTO> body = actualSignInResult.getBody();
        assertEquals(401, body.getStatus());
        assertEquals("Incorrect password!", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).existsByUsername(Mockito.<String>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthController#signIn(SigninDTO)}
     */
    @Test
    public void testSignIn2() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new InvalidCredentialsException("An error occurred", HttpStatus.CONTINUE));
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(new AuthService(authenticationManager, jwtTokenProvider,
                userRepository, passwordEncoder, new ModelMapper(), mock(RoleRepository.class)));
        ResponseEntity<ResponseDTO<JwtResponseDTO>> actualSignInResult = authController
                .signIn(new SigninDTO("janedoe", "iloveyou"));
        assertTrue(actualSignInResult.hasBody());
        assertTrue(actualSignInResult.getHeaders().isEmpty());
        assertEquals(100, actualSignInResult.getStatusCodeValue());
        ResponseDTO<JwtResponseDTO> body = actualSignInResult.getBody();
        assertEquals(100, body.getStatus());
        assertEquals("An error occurred", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).existsByUsername(Mockito.<String>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthController#signIn(SigninDTO)}
     */
    @Test
    public void testSignIn3() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getPassword())
                .thenThrow(new InvalidCredentialsException("An error occurred", HttpStatus.CONTINUE));
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(new AuthService(authenticationManager, jwtTokenProvider,
                userRepository, passwordEncoder, new ModelMapper(), mock(RoleRepository.class)));
        ResponseEntity<ResponseDTO<JwtResponseDTO>> actualSignInResult = authController
                .signIn(new SigninDTO("janedoe", "iloveyou"));
        assertTrue(actualSignInResult.hasBody());
        assertTrue(actualSignInResult.getHeaders().isEmpty());
        assertEquals(100, actualSignInResult.getStatusCodeValue());
        ResponseDTO<JwtResponseDTO> body = actualSignInResult.getBody();
        assertEquals(100, body.getStatus());
        assertEquals("An error occurred", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).existsByUsername(Mockito.<String>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
        verify(userEntity).getPassword();
    }

    /**
     * Method under test: {@link AuthController#signIn(SigninDTO)}
     */
    @Test
    public void testSignIn4() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getPassword())
                .thenThrow(new InvalidCredentialsException("An error occurred", HttpStatus.CONTINUE));
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(false);
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(new AuthService(authenticationManager, jwtTokenProvider,
                userRepository, passwordEncoder, new ModelMapper(), mock(RoleRepository.class)));
        ResponseEntity<ResponseDTO<JwtResponseDTO>> actualSignInResult = authController
                .signIn(new SigninDTO("janedoe", "iloveyou"));
        assertTrue(actualSignInResult.hasBody());
        assertTrue(actualSignInResult.getHeaders().isEmpty());
        assertEquals(100, actualSignInResult.getStatusCodeValue());
        ResponseDTO<JwtResponseDTO> body = actualSignInResult.getBody();
        assertEquals(100, body.getStatus());
        assertEquals("An error occurred", body.getMessage());
        assertNull(body.getContent());
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
        verify(userEntity).getPassword();
    }

    /**
     * Method under test: {@link AuthController#signIn(SigninDTO)}
     */
    @Test
    public void testSignIn5() {

        new InvalidCredentialsException("An error occurred", HttpStatus.CONTINUE);

        AuthService userService = mock(AuthService.class);
        when(userService.signInUser(Mockito.<SigninDTO>any())).thenReturn("Sign In User");
        AuthController authController = new AuthController(userService);
        ResponseEntity<ResponseDTO<JwtResponseDTO>> actualSignInResult = authController
                .signIn(new SigninDTO("janedoe", "iloveyou"));
        assertTrue(actualSignInResult.hasBody());
        assertTrue(actualSignInResult.getHeaders().isEmpty());
        assertEquals(200, actualSignInResult.getStatusCodeValue());
        ResponseDTO<JwtResponseDTO> body = actualSignInResult.getBody();
        assertEquals(200, body.getStatus());
        assertEquals("Logged in successfully!", body.getMessage());
        JwtResponseDTO content = body.getContent();
        assertEquals("Sign In User", content.getAccessToken());
        assertEquals("Bearer", content.getTokenType());
        verify(userService).signInUser(Mockito.<SigninDTO>any());
    }

    /**
     * Method under test: {@link AuthController#signIn(SigninDTO)}
     */
    @Test
    public void testSignIn6() {

        new InvalidCredentialsException("An error occurred", HttpStatus.CONTINUE);

        AuthService userService = mock(AuthService.class);
        when(userService.signInUser(Mockito.<SigninDTO>any()))
                .thenThrow(new InvalidCredentialsException("An error occurred", HttpStatus.CONTINUE));
        AuthController authController = new AuthController(userService);
        ResponseEntity<ResponseDTO<JwtResponseDTO>> actualSignInResult = authController
                .signIn(new SigninDTO("janedoe", "iloveyou"));
        assertTrue(actualSignInResult.hasBody());
        assertTrue(actualSignInResult.getHeaders().isEmpty());
        assertEquals(100, actualSignInResult.getStatusCodeValue());
        ResponseDTO<JwtResponseDTO> body = actualSignInResult.getBody();
        assertEquals(100, body.getStatus());
        assertEquals("An error occurred", body.getMessage());
        assertNull(body.getContent());
        verify(userService).signInUser(Mockito.<SigninDTO>any());
    }

    /**
     * Method under test: {@link AuthController#signUp(SingupDTO, BindingResult)}
     */
    @Test
    public void testSignUp() {

        AuthService userService = mock(AuthService.class);
        doNothing().when(userService).signUpUser(Mockito.<SingupDTO>any());
        AuthController authController = new AuthController(userService);
        SingupDTO signupDto = new SingupDTO();
        ResponseEntity<ResponseDTO<Object>> actualSignUpResult = authController.signUp(signupDto,
                new BindException("Target", "Object Name"));
        assertTrue(actualSignUpResult.hasBody());
        assertTrue(actualSignUpResult.getHeaders().isEmpty());
        assertEquals(201, actualSignUpResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualSignUpResult.getBody();
        assertEquals(201, body.getStatus());
        assertEquals("Signed up successfully!", body.getMessage());
        assertNull(body.getContent());
        verify(userService).signUpUser(Mockito.<SingupDTO>any());
    }

    /**
     * Method under test: {@link AuthController#signUp(SingupDTO, BindingResult)}
     */
    @Test
    public void testSignUp2() {

        AuthController authController = new AuthController(mock(AuthService.class));
        SingupDTO signupDto = new SingupDTO();
        BeanPropertyBindingResult bindingResult = mock(BeanPropertyBindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());
        when(bindingResult.hasErrors()).thenReturn(true);
        ResponseEntity<ResponseDTO<Object>> actualSignUpResult = authController.signUp(signupDto, bindingResult);
        assertTrue(actualSignUpResult.hasBody());
        assertTrue(actualSignUpResult.getHeaders().isEmpty());
        assertEquals(400, actualSignUpResult.getStatusCodeValue());
        ResponseDTO<Object> body = actualSignUpResult.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("", body.getMessage());
        assertNull(body.getContent());
        verify(bindingResult).hasErrors();
        verify(bindingResult).getAllErrors();
    }
}

