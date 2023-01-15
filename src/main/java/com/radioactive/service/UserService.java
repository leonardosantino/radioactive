package com.radioactive.service;

import com.radioactive.domain.document.User;
import com.radioactive.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repository;

    public Mono<User> save(User user) {
        return repository.save(user);
    }

    public Mono<Void> update(String id, User user) {
        user.setId(id);

        return findById(id).map((u) -> save(user)).thenEmpty(Mono.empty());
    }

    public Mono<User> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).log();
    }

    public Flux<User> findAll() {
        return repository.findAll();
    }

    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

}
