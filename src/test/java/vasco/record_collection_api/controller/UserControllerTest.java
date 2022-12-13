package vasco.record_collection_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import vasco.record_collection_api.exceptions.AuthenticationException;
import vasco.record_collection_api.exceptions.BusinessRuleException;
import vasco.record_collection_api.model.entity.User;
import vasco.record_collection_api.service.MusicRecordService;
import vasco.record_collection_api.service.UserService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    static final String API = "/api/users";

    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService service;

    @MockBean
    MusicRecordService recordService;

    @Test
    public void shouldLoginAUser() throws Exception{
        String email = "user@mail.com";
        String password = "pass";
        User user = User.builder().id(1l).name("name").email(email).password(password).build();

        Mockito.when(service.authentication(email, password)).thenReturn(user);
        String json = new ObjectMapper().writeValueAsString(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/login")).accept(JSON).contentType(JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("password").value(user.getPassword()));
    }

    @Test
    public void shouldReturnABadRequestWhenFailToLogin() throws Exception{
        String email = "user@mail.com";
        String password = "pass";
        User user = User.builder().id(1l).name("name").email(email).password(password).build();

        Mockito.when(service.authentication(email, password)).thenThrow(AuthenticationException.class);
        String json = new ObjectMapper().writeValueAsString(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/login")).accept(JSON).contentType(JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldRegisterANewUser() throws Exception{
        String email = "user@mail.com";
        String password = "pass";
        User user = User.builder().id(1l).name("name").email(email).password(password).build();

        Mockito.when(service.registration(Mockito.any(User.class))).thenReturn(user);
        String json = new ObjectMapper().writeValueAsString(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API).accept(JSON).contentType(JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("password").value(user.getPassword()));
    }

    @Test
    public void shouldReturnABadRequestWhenFailToRegisterAUser() throws Exception{
        String email = "user@mail.com";
        String password = "pass";
        User user = User.builder().id(1l).name("name").email(email).password(password).build();

        Mockito.when(service.registration(Mockito.any(User.class))).thenThrow(BusinessRuleException.class);
        String json = new ObjectMapper().writeValueAsString(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API).accept(JSON).contentType(JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
