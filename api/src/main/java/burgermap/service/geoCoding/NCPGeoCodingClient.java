package burgermap.service.geoCoding;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@ConfigurationProperties(prefix = "cloud.ncp.maps.geocoding")
@Component
public class NCPGeoCodingClient implements GeoCodingClient {
    @Setter
    private String clientId;
    @Setter
    private String clientSecret;

    private WebClient webClient;

    @PostConstruct
    void init() {
        System.out.println("clientId = " + clientId);
        this.webClient = WebClient.builder().baseUrl("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode")
                .defaultHeader("x-ncp-apigw-api-key-id", clientId)
                .defaultHeader("x-ncp-apigw-api-key", clientSecret)
                .build();
    }

    @Override
    public String request(String address) {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .queryParam("query", address)
                                .build())
                .retrieve()
                .bodyToMono(String.class)  // JSON 형식의 응답을 String으로 변환
                .block();
    }
}
