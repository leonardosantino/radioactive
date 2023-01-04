package com.radioactive.flux;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Slf4j
public class FluxTest {

    @Test
    void fluxSubscriber() {

        Flux<String> f = Flux.just("Leonardo", "Santino", "Lima").log();

        StepVerifier.create(f)
                .expectNext("Leonardo", "Santino", "Lima")
                .verifyComplete();
    }

    @Test
    void fluxSubscriberFromList() {

        Flux<String> f = Flux.just("Leonardo", "Santino", "Lima").log();

        StepVerifier.create(f)
                .expectNext("Leonardo", "Santino", "Lima")
                .verifyComplete();
    }

    @Test
    void OperatorsTest() {
        // Concurrency agnostic - se não utilizar concorrencia vai executar na thread main

        Flux<Integer> flux = Flux.range(1, 4)
                .map(i -> {
                    log.info("Map 1 - Number {} on thread {}", i, Thread.currentThread().getName());
                    return i;
                })
                .subscribeOn(Schedulers.single())
                .map(i -> {
                    log.info("Map 2 - Number {} on thread {}", i, Thread.currentThread().getName());
                    return i;
                });
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    void PublishOnSimple() {
        // Concurrency agnostic - se não utilizar concorrencia vai executar na thread main

        Flux<Integer> flux = Flux.range(1, 4)
                .map(i -> {
                    log.info("Map 1 - Number {} on thread {}", i, Thread.currentThread().getName());
                    return i;
                })
                .publishOn(Schedulers.boundedElastic())
                .map(i -> {
                    log.info("Map 2 - Number {} on thread {}", i, Thread.currentThread().getName());
                    return i;
                });
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    void concatOperator() {
        // Concurrency agnostic - se não utilizar concorrencia vai executar na thread main

        Flux<String> fluxA = Flux.just("a", "b");
        Flux<String> fluxB = Flux.just("c", "d");

        Flux<String> concatFlux = Flux.concat(fluxA, fluxB).log();

        StepVerifier.create(concatFlux)
                .expectSubscription()
                .expectNext("a", "b", "c", "d")
                .verifyComplete();
    }
}
