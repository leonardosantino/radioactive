package com.radioactive.service;

import com.radioactive.domain.document.User;
import com.radioactive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public Mono<User> save(User user) {
        return repository.save(user);
    }

    public Mono<User> findById(String id) {
        return repository.findById(id);
    }
    public Flux<User> findAll() {
        return repository.findAll();
    }

}
