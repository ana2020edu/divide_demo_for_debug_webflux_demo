package com.example.divide_demo_for_debug_webflux_demo;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MyService {
    public Flux<Integer> divide(Integer num) {
        return Flux.just(10, 20)
                .map(x -> x / num); // Simply divide by the number entered
    }
}
