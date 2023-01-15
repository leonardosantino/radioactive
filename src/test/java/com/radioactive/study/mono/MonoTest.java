package com.radioactive.study.mono;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class MonoTest {
    @Test
    void contextLoads() {

        String name = "Leonardo";

        Mono<String> mono = Mono.just(name).log();

        mono.subscribe();

        StepVerifier.create(mono)
                .expectNext("Leonardo").verifyComplete();

        log.info("Mono {}", mono);
    }

    @Test
    void monoSubscriberConsumer() {

        String name = "Leonardo";

        Mono<String> mono = Mono.just(name).log();

        mono.subscribe(it -> log.info("value {}", it));
    }

    @Test
    void monoSubscriberConsumerError() {

        String name = "Leonardo";

        Mono<String> mono = Mono.just(name).map(it -> {
            throw new RuntimeException("Mono Error");
        });

        mono.subscribe(it -> log.info("value {}", it), it -> log.error("Something bad"));

        StepVerifier.create(mono).expectError(RuntimeException.class).verify();
    }

    @Test
    void monoSubscriberConsumerOnComplete() {

        String name = "Leonardo";

        Mono<String> mono = Mono.just(name).log().map(String::toUpperCase);

        mono.subscribe(it -> log.info("On next"), it -> log.error("On Error"), () -> log.info("On complete"));

        StepVerifier.create(mono).expectNext(name).verifyComplete();
    }

    @Test
    void monoSubscriberConsumeSubscription() {

        String name = "Leonardo";

        Mono<String> mono = Mono.just(name).log().map(String::toUpperCase);

        mono.subscribe
                (
                        it -> log.info("On next"),
                        it -> log.error("On Error"),
                        () -> log.info("On complete"),
                        subscription -> subscription.request(5)
                );

    }

    @Test
    void monoSubscriberConsumerDoOnMethods() {

        String name = "Leonardo";

        Mono<String> mono = Mono.just(name)
                .log()
                .map(String::toUpperCase)
                .doOnSubscribe(subscription -> log.info("doOnSubscribe {}", subscription))
                .doOnRequest(value -> log.info("doOnRequest {}", value))
                .doOnNext(s -> log.info("doOnNext {}", s))
                .doOnSuccess(s -> log.info("doOnSuccess {}", s));

        mono.subscribe
                (
                        s -> log.info("doOnNext {}", s),
                        throwable -> log.warn("throwable {}", throwable.getMessage()),
                        () -> log.info("On complete")
                );

    }

    @Test
    void monoDoOnError() {

        Mono<Object> error = Mono.error(new IllegalArgumentException("Illegal argument exception"))
                .onErrorReturn("onErrorReturn")
                .doOnError(throwable -> log.error("doOnError {}", throwable.getMessage()))

                .onErrorResume(throwable -> {
                    log.warn("onErrorResume {}", throwable.getMessage());
                    return Mono.just("On error resume");
                })
                .log();

        StepVerifier.create(error).expectError(IllegalArgumentException.class).verify();
    }
}
