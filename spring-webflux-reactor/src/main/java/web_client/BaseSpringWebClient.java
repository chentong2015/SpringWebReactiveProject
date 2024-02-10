package web_client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

// Spring Web Client 废弃使用
// 1. A part of Spring WebFlux framework (parallel version of Spring MVC)
// 2. Spring webflux uses project reactor as reactive library.
public class BaseSpringWebClient {

    private final Logger logger = LogManager.getLogger(BaseSpringWebClient.class);

    // TODO. 在使用builder构建WebClient时进行Authentication
    public void buildWebClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost/test")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth("admin", "password"))
                .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth("<bearer token>"))
                .build();
        System.out.println(webClient);

        WebClient client = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication("username", "token"))
                .build();
        Mono<String> result = client.get()
                .uri("/customers")
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("username : token").getBytes(StandardCharsets.UTF_8)))
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(result);
    }

    // TODO: Client Side 客户端使用WebClient to consume a SSE endpoint
    // client.get()
    //    .uri("url")
    //    .accept(MediaType.TEXT_EVENT_STREAM)
    //    .retrieve()
    //    .bodyToFlux(Message::class.java)
    public void consumeServerSentEvent() {
        WebClient client = WebClient.create("http://localhost:8080/sse-server");

        Mono<String> response = client.get()
                .uri("/test")
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(response);

        // TypeReference类型和SSE Server端定义的返回类型是一致
        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/stream-sse")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<>() {
                });  // 提供类型的映射
        // subscribe 订阅event事件，SSE Server端会进行事件的推送
        // 定义收到event事件，出错以及Completed完成之后如何处理
        eventStream.subscribe(
                content -> logger.info("Time: {} - event: name[{}], id [{}], content[{}] ",
                        LocalTime.now(), content.event(), content.id(), content.data()),
                error -> logger.error("Error receiving SSE: {}", error),
                () -> logger.info("Completed!!!"));
    }
}
