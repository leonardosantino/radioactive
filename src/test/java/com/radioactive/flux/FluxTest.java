package com.radioactive.flux;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple3;

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

    @Test
    void flatMapOperator() {
        Flux<String> fluxA = Flux.just("a", "b");

        Flux<String> fluxB = fluxA.map(s -> s.toUpperCase())
                .flatMap(s -> this.findByName(s))
                .log();

        fluxB.subscribe(stringFlux -> log.info(stringFlux.toString()));

        StepVerifier
                .create(fluxB)
                .expectSubscription()
                .expectNext("NameA1", "NameA2", "NameB1", "NameB2")
                .verifyComplete();
    }


    public Flux<String> findByName(String name) {
        return name.equals("A") ? Flux.just("NameA1", "NameA2") : Flux.just("NameB1", "NameB2");
    }

    @Test
    void zipOperator() {
        Flux<String> title = Flux.just("Grand Blue", "Baki");
        Flux<String> studio = Flux.just("Zero-G", "TMS Entertainment");
        Flux<Integer> episodes = Flux.just(12, 24);

        Flux<Tuple3<String, String, Integer>> tp = Flux.zip(title, studio, episodes);

        Flux<Anime> anime = tp.flatMap(o -> Flux.just(new Anime(o.getT1(), o.getT2(), o.getT3())))
                .log();

        anime.subscribe(a -> log.info(a.toString()));

        StepVerifier
                .create(anime)
                .expectSubscription()
                .expectNext(
                        new Anime("Grand Blue", "Zero-G", 12),
                        new Anime("Baki", "TMS Entertainment", 24)
                );
    }

    @AllArgsConstructor
    @ToString
    public class Anime {
        private String title;
        private String studio;
        private Integer episodes;
    }
}
