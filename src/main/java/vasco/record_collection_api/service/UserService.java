package vasco.record_collection_api.service;

import vasco.record_collection_api.model.entity.User;

import java.util.Optional;

public interface UserService {

    User authentication (String email, String password);

    User registration(User user);

    void emailValidation(String email);

    Optional<User> getUserById(Long id);
}
