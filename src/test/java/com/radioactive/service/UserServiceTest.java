package com.radioactive.service;

import com.radioactive.domain.document.User;
import com.radioactive.fixtures.UserFixture;
import com.radioactive.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    @DisplayName("should return user when call find by id")
    void findById() {
        String id = "1";
        User user = UserFixture.one();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(user));

        StepVerifier.create(service.findById(id))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("should throw error when call find by id")
    void findByIdOnEmpty() {
        String id = "1";
        User user = UserFixture.one();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(service.findById(id))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("should return all users")
    void findAll() {
        List<User> users = UserFixture.listOfUsers();

        BDDMockito.when(repository.findAll())
                        .thenReturn(Flux.just(users.get(0), users.get(1)));

        StepVerifier.create(service.findAll())
                .expectSubscription()
                .expectNext(users.get(0), users.get(1))
                .verifyComplete();
    }

    @Test
    void deleteById() {
    }
}