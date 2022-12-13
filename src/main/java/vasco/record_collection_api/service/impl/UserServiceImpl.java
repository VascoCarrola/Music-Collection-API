package vasco.record_collection_api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vasco.record_collection_api.exceptions.AuthenticationException;
import vasco.record_collection_api.exceptions.BusinessRuleException;
import vasco.record_collection_api.model.entity.User;
import vasco.record_collection_api.model.repository.UserRepository;
import vasco.record_collection_api.service.UserService;


import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    @Autowired
    public UserServiceImpl(UserRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public User authentication(String email, String password) {
        Optional<User> user = repository.findByEmail(email);

        if(!user.isPresent()){throw new AuthenticationException("The email is incorrect!");}
        if(!user.get().getPassword().equals(password)){throw new AuthenticationException("the password is incorrect!");}
        return user.get();
    }

    @Override
    @Transactional
    public User registration(User user) {
        emailValidation(user.getEmail());
        return repository.save(user);
    }

    @Override
    public void emailValidation(String email) {
        boolean exist = repository.existsByEmail(email);
        if(exist){throw new BusinessRuleException("Email already taken!");}
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }
}
