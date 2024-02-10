package rest_template;

import org.springframework.web.client.RestTemplate;

// 推荐使用RestTemplate/AsyncRestTemplate发送请求，单元测试Endpoints
public class BaseSpringRestTemplate {

    // RestTemplate提供的最常规的Web Request请求操作
    public void testGet() {
        final String uri = "http://localhost:8080/springrestexample/hello";
        RestTemplate restTemplate = new RestTemplate();
        String resultGet = restTemplate.getForObject(uri, String.class);
        String resultPost = restTemplate.postForObject(uri, "post item", String.class);

        System.out.println(resultGet + resultPost);
        restTemplate.put(uri, "put request data");
        restTemplate.delete(uri);
    }
}
