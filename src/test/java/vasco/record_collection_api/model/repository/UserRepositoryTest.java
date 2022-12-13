package vasco.record_collection_api.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import vasco.record_collection_api.model.entity.User;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    TestEntityManager entityManager;

    public static String email = "name@mail.com";

    public static User createUser(){
        return User.builder().name("name").email(email).build();
    }

    @Test
    public void shouldReturnFalseIfEmailDoNotExistsInDatabase(){
        boolean result = repository.existsByEmail(email);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void checkingEmailExistence(){
        User user = createUser();
        entityManager.persist(user);

        boolean result = repository.existsByEmail(email);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void shouldPersistAnUserInDatabase(){
        User user = createUser();

        User persistedUser = repository.save(user);

        Assertions.assertThat(persistedUser.getId()).isNotNull();
    }

    @Test
    public void shouldReturnNothingIfDoesNotExistAUserWhitThatEmail(){
        Optional<User> user = repository.findByEmail(email);

        Assertions.assertThat(user.isPresent()).isFalse();
    }
}
