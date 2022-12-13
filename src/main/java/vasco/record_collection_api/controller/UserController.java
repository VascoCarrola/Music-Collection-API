package vasco.record_collection_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vasco.record_collection_api.exceptions.AuthenticationException;
import vasco.record_collection_api.exceptions.BusinessRuleException;
import vasco.record_collection_api.model.entity.User;
import vasco.record_collection_api.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody User user){
        try{
            User authenticatedUser = service.authentication(user.getEmail(), user.getPassword());
            return new ResponseEntity(authenticatedUser, HttpStatus.OK);
        }catch (AuthenticationException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity registerUser(@RequestBody User user){
        try{
            User registeredUser = service.registration(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        }catch (BusinessRuleException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
