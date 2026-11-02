package cl.duoc.pedidoscoffee360_admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

@Configuration
public class RabbitAdminConfig {

    @Bean
    public RestClient rabbitAdminRestClient(@Value("${rabbit.admin.url}") String url,
                                            @Value("${rabbit.admin.username}") String username,
                                            @Value("${rabbit.admin.password}") String password) {
        HttpClient http11 = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
        return RestClient.builder()
            .requestFactory(new JdkClientHttpRequestFactory(http11))
            .baseUrl(url)
            .defaultHeaders(headers ->
                headers.setBasicAuth(username, password)
            )
            .build();
    }
}
