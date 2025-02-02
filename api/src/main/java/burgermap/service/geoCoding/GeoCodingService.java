package burgermap.service.geoCoding;

import burgermap.dto.geo.GeoLocation;
import burgermap.exception.geoCoding.InvalidAddressException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeoCodingService {
    private final NCPGeoCodingClient geoCodingClient;

    /**
     * 주소를 입력받아 해당 주소의 위도, 경도 정보를 반환
     *
     * @param address 주소
     * @return 주소의 위도, 경도 정보
     */
    public Optional<GeoLocation> getGeoLocation(String address) {
        String response = geoCodingClient.request(address);

        Map<String, Object> parsedResponse = parseResponse(response);
        if (parsedResponse.isEmpty()) {
            return Optional.empty();
        }

        if (((List) parsedResponse.get("addresses")).isEmpty())
            throw new InvalidAddressException(address);

        Map<String, Object> addressInfo = getFirstAddressInfo(parsedResponse);
        return Optional.of(
                new GeoLocation(
                        Double.parseDouble((String) addressInfo.get("y")),
                        Double.parseDouble((String) addressInfo.get("x"))));
    }

    /**
     * API의 JSON 응답을 파싱하여 Map 객체로 반환
     *
     * @param response API 응답(JSON 형식)
     * @return 파싱된 Map 객체
     */
    private Map<String, Object> parseResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map;
        try {
            map = objectMapper.readValue(response, HashMap.class);
        } catch (JsonProcessingException e) {
            map = new HashMap<>();
        }
        return map;
    }

    /**
     * 파싱된 응답에서 첫 번째 주소 정보를 반환
     *
     * @param parsedResponse 파싱된 응답
     * @return 첫 번째 주소 정보
     */
    private Map<String, Object> getFirstAddressInfo(Map<String, Object> parsedResponse) {
        List<Map<String, Object>> addresses = (List<Map<String, Object>>) parsedResponse.get("addresses");
        return addresses.get(0);
    }
}
