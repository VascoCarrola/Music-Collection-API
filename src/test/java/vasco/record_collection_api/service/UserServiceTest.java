package vasco.record_collection_api.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import vasco.record_collection_api.exceptions.AuthenticationException;
import vasco.record_collection_api.exceptions.BusinessRuleException;
import vasco.record_collection_api.model.entity.User;
import vasco.record_collection_api.model.repository.UserRepository;
import vasco.record_collection_api.service.impl.UserServiceImpl;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @SpyBean
    UserServiceImpl service;

    @MockBean
    UserRepository repository;

    @Test(expected = Test.None.class)
    public void shouldAuthenticateUserSuccessfully(){
        String email = "user@mail.com";
        String password = "pass";
        User user = User.builder().email(email).password(password).build();

        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        User authenticatedUser = service.authentication(email,password);
        Assertions.assertThat(authenticatedUser).isNotNull();
        Assertions.assertThat(authenticatedUser.getPassword()).isEqualTo(password);
    }

    @Test
    public void shouldThrowExceptionAuthenticatingUserWithInvalidEmail(){
        String email = "user@mail.com";
        String password = "pass";

        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> service.authentication(email, password));
        Assertions.assertThat(exception).isInstanceOf(AuthenticationException.class).hasMessage("The email is incorrect!");
    }

    @Test
    public void shouldThrowExceptionAuthenticatingUserWithInvalidPassword(){
        String email = "user@mail.com";
        String password = "pass";
        String falsePassword = "p";
        User user = User.builder().email(email).password(password).build();

        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        Throwable exception = Assertions.catchThrowable(() -> service.authentication(email, falsePassword));
        Assertions.assertThat(exception).isInstanceOf(AuthenticationException.class).hasMessage("the password is incorrect!");
    }

    @Test(expected = Test.None.class)// do not expect any exception
    public void shouldRegisterUser(){
        User user = User.builder().id(1l).name("name").email("email").password("pass").build();
        Mockito.doNothing().when(service).emailValidation(Mockito.anyString());
        Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);

        User registeredUser = service.registration(new User());

        Assertions.assertThat(registeredUser).isNotNull();
        Assertions.assertThat(registeredUser.getId()).isEqualTo(user.getId());
        Assertions.assertThat(registeredUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(registeredUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(registeredUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test(expected = BusinessRuleException.class)
    public void shouldNotRegisterUserWithInvalidEmail(){
        String email = "email@com";
        User user = User.builder().email(email).build();
        Mockito.doThrow(BusinessRuleException.class).when(service).emailValidation(email);

        service.registration(user);
        Mockito.verify(repository, Mockito.never()).save(user);
    }

    @Test(expected = Test.None.class)
    public void shouldValidateEmail(){
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

        service.emailValidation("email.com");
    }

    @Test(expected = BusinessRuleException.class)
    public void shouldThrowExceptionWhenValidatingInvalidEmail(){
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        service.emailValidation("email.com");
    }
}