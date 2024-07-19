package com.example.authorizationserver;

import com.example.authorizationserver.dto.AuthorizationRequest;
import com.example.authorizationserver.dto.AuthorizationResponse;
import com.example.authorizationserver.dto.LoginRequest;
import com.example.authorizationserver.dto.RegisterRequest;
import com.example.authorizationserver.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AuthController.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class})
class AuthControllerDiffblueTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthorizationService authorizationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLogin() throws Exception {
        // Arrange
        when(userService.login(Mockito.any(), Mockito.any())).thenReturn("Login");

        LoginRequest loginRequest = new LoginRequest("janedoe", "iloveyou");
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // Act and Assert
        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"Login\"}"));
    }

    /**
     * Method under test: {@link AuthController#login(LoginRequest)}
     */
    @Test
    void testLogin2() throws Exception {
        // Arrange
        when(userService.login(Mockito.<String>any(), Mockito.<String>any())).thenReturn("Login");
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new LoginRequest("janedoe", "iloveyou")));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"Login\"}"));
    }

    @Test
    void testRegister() throws Exception {
        // Arrange
        when(userService.register(Mockito.any(RegisterRequest.class))).thenReturn("Register");

        RegisterRequest registerRequest = new RegisterRequest("janedoe", "iloveyou", "jane.doe@example.org", "6625550144");
        String registerRequestJson = objectMapper.writeValueAsString(registerRequest);

        // Act and Assert
        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"Register\"}"));
    }

    /**
     * Method under test: {@link AuthController#register(RegisterRequest)}
     */
    @Test
    void testRegister2() throws Exception {
        // Arrange
        when(userService.register(Mockito.<RegisterRequest>any())).thenReturn("Register");
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper
                .writeValueAsString(new RegisterRequest("janedoe", "iloveyou", "jane.doe@example.org", "6625550144")));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"Register\"}"));
    }

    /**
     * Method under test: {@link AuthController#authorize(AuthorizationRequest)}
     */
    @Test
    void testAuthorize() throws Exception {
        // Arrange
        UserResponse.UserResponseBuilder phoneNumberResult = UserResponse.builder()
                .email("jane.doe@example.org")
                .id(1L)
                .phoneNumber("6625550144");
        UserResponse user = phoneNumberResult.roles(new HashSet<>()).username("janedoe").build();
        when(authorizationService.authorize(Mockito.<AuthorizationRequest>any()))
                .thenReturn(new AuthorizationResponse(user, true));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/api/v1/auth/authorize")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new AuthorizationRequest("Name", "ABC123", "Path")));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"user\":{\"id\":1,\"username\":\"janedoe\",\"email\":\"jane.doe@example.org\",\"phoneNumber\":\"6625550144\",\"roles"
                                        + "\":[]},\"authorized\":true}"));
    }

    /**
     * Method under test: {@link AuthController#authorize(AuthorizationRequest)}
     */
    @Test
    void testAuthorize2() throws Exception {
        // Arrange
        UserResponse.UserResponseBuilder phoneNumberResult = UserResponse.builder()
                .email("jane.doe@example.org")
                .id(1L)
                .phoneNumber("6625550144");
        UserResponse user = phoneNumberResult.roles(new HashSet<>()).username("janedoe").build();
        when(authorizationService.authorize(Mockito.<AuthorizationRequest>any()))
                .thenReturn(new AuthorizationResponse(user, false));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/api/v1/auth/authorize")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new AuthorizationRequest("Name", "ABC123", "Path")));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"user\":{\"id\":1,\"username\":\"janedoe\",\"email\":\"jane.doe@example.org\",\"phoneNumber\":\"6625550144\",\"roles"
                                        + "\":[]},\"authorized\":false}"));
    }
}