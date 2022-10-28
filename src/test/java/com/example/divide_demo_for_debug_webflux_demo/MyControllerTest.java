package com.example.divide_demo_for_debug_webflux_demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Hooks;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void divide() {
        Hooks.onOperatorDebug();
        webTestClient.get().uri("/divide/10")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(x -> assertThat(x).isEqualTo("[1,2]"));
    }

    @Test
    void divideByZero() {
        Hooks.onOperatorDebug();
        webTestClient.get().uri("/divide/0") // Oops! Divide by zero! ArithmeticException!
                .exchange()
                .expectStatus().isOk();
        /*
            We can see detailed logs. It is not fragmented by thread because of 'Hooks.onOperatorDebug()'
            It's very useful for debugging.

            java.lang.ArithmeticException: / by zero
                at com.example.divide_demo_for_debug_webflux_demo.MyService.lambda$divide$0(MyService.java:10) ~[main/:na]
                Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:
            Assembly trace from producer [reactor.core.publisher.FluxMapFuseable] :
                reactor.core.publisher.Flux.map(Flux.java:6271)
                com.example.divide_demo_for_debug_webflux_demo.MyService.divide(MyService.java:10)
            Error has been observed at the following site(s):
                *____________Flux.map ? at com.example.divide_demo_for_debug_webflux_demo.MyService.divide(MyService.java:10)
                |_   Flux.collectList ? at org.springframework.http.codec.json.AbstractJackson2Encoder.encode(AbstractJackson2Encoder.java:187)
                |_           Mono.map ? at org.springframework.http.codec.json.AbstractJackson2Encoder.encode(AbstractJackson2Encoder.java:188)
                |_          Mono.flux ? at org.springframework.http.codec.json.AbstractJackson2Encoder.encode(AbstractJackson2Encoder.java:189)
                |_     Mono.doOnError ? at org.springframework.http.server.reactive.AbstractServerHttpResponse.writeWith(AbstractServerHttpResponse.java:241)
                |_         checkpoint ? Handler com.example.divide_demo_for_debug_webflux_demo.MyController#divide(Integer) [DispatcherHandler]
                |_ Mono.onErrorResume ? at org.springframework.web.reactive.DispatcherHandler.handleResult(DispatcherHandler.java:181)
                *________Mono.flatMap ? at org.springframework.web.reactive.DispatcherHandler.lambda$handleResult$5(DispatcherHandler.java:182)
                *________Mono.flatMap ? at org.springframework.web.reactive.DispatcherHandler.handle(DispatcherHandler.java:154)
                *__________Mono.defer ? at org.springframework.web.server.handler.DefaultWebFilterChain.filter(DefaultWebFilterChain.java:119)
                |_ Mono.onErrorResume ? at org.springframework.web.server.handler.ExceptionHandlingWebHandler.handle(ExceptionHandlingWebHandler.java:77)
                *__________Mono.error ? at org.springframework.web.server.handler.ExceptionHandlingWebHandler$CheckpointInsertingHandler.handle(ExceptionHandlingWebHandler.java:98)
                |_         checkpoint ? HTTP GET "/divide/0" [ExceptionHandlingWebHandler]
            Original Stack Trace:
                ...
        */
    }
}