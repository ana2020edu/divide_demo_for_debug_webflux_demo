package com.example.divide_demo_for_debug_webflux_demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class MyController {
    private final MyService myService;

    public MyController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("/divide/{num}")
    public Flux<Integer> divide(@PathVariable Integer num) {
        return myService.divide(num);
    }
}
