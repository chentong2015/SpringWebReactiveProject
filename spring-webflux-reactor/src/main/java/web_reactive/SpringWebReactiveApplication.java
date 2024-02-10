package web_reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class SpringWebReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebReactiveApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> routes() {
        String message = "Spring Native and Beyond!";
        return RouterFunctions.route()
                .GET("/", request -> ok().body(Mono.just(message), String.class))
                .build();
    }
}
