package burgermap.service.geoCoding;

import burgermap.dto.geo.GeoLocation;
import burgermap.exception.geoCoding.InvalidAddressException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GeoCodingServiceTest {

    @InjectMocks
    private GeoCodingService geoCodingService;

    @Mock
    private NCPGeoCodingClient geoCodingClient;

    @Test
    @DisplayName("정확한 주소를 입력하여 받은 응답을 파싱하여 위경도 정보 획득")
    void getGeoLocation() {
        // given
        String address = "정확한 주소";
        String response = "{\"status\":\"OK\",\"meta\":{\"totalCount\":1,\"page\":1,\"count\":1},\"addresses\":[{\"roadAddress\":\"서울특별시 중구 명동7길 8\",\"jibunAddress\":\"서울특별시 중구 명동1가 48-2\",\"englishAddress\":\"8, Myeongdong 7-gil, Jung-gu, Seoul, Republic of Korea\",\"addressElements\":[{\"types\":[\"SIDO\"],\"longName\":\"서울특별시\",\"shortName\":\"서울특별시\",\"code\":\"\"},{\"types\":[\"SIGUGUN\"],\"longName\":\"중구\",\"shortName\":\"중구\",\"code\":\"\"},{\"types\":[\"DONGMYUN\"],\"longName\":\"명동1가\",\"shortName\":\"명동1가\",\"code\":\"\"},{\"types\":[\"RI\"],\"longName\":\"\",\"shortName\":\"\",\"code\":\"\"},{\"types\":[\"ROAD_NAME\"],\"longName\":\"명동7길\",\"shortName\":\"명동7길\",\"code\":\"\"},{\"types\":[\"BUILDING_NUMBER\"],\"longName\":\"8\",\"shortName\":\"8\",\"code\":\"\"},{\"types\":[\"BUILDING_NAME\"],\"longName\":\"\",\"shortName\":\"\",\"code\":\"\"},{\"types\":[\"LAND_NUMBER\"],\"longName\":\"48-2\",\"shortName\":\"48-2\",\"code\":\"\"},{\"types\":[\"POSTAL_CODE\"],\"longName\":\"04534\",\"shortName\":\"04534\",\"code\":\"\"}],\"x\":\"126.9847333\",\"y\":\"37.5642362\",\"distance\":0.0}],\"errorMessage\":\"\"}";
        Mockito.when(geoCodingClient.request(address))
                .thenAnswer(invocation -> response);

        // when
        Optional<GeoLocation> geoLocation = geoCodingService.getGeoLocation(address);

        // then
        assertThat(geoLocation.isPresent()).isTrue();
        assertThat(geoLocation.get().getLatitude()).isEqualTo(37.5642362);
        assertThat(geoLocation.get().getLongitude()).isEqualTo(126.9847333);
    }

    @Test
    @DisplayName("잘못된 주소 입력하면 InvalidAddressException 예외 발생")
    void getGeoLocationWithUnregisteredAddress() {
        // given
        String address = "잘못된 주소";
        String response = "{\"status\":\"OK\",\"meta\":{\"totalCount\":0,\"count\":0},\"addresses\":[],\"errorMessage\":\"\"}";
        Mockito.when(geoCodingClient.request(address))
                .thenAnswer(invocation -> response);

        // when
        // then
        assertThatThrownBy(() -> geoCodingService.getGeoLocation(address))
                .isInstanceOf(InvalidAddressException.class);
    }

    @Test
    @DisplayName("주소가 부정확해 다수의 결과가 주어지면 첫 번째 주소의 위경도 정보 반환")
    void getFirstAddressGeoLocation() {
        // given
        String address = "부정확한 주소";
        String response = "{\"status\":\"OK\",\"meta\":{\"totalCount\":2,\"page\":1,\"count\":2},\"addresses\":[{\"roadAddress\":\"서울특별시 중구 명동7길 8\",\"jibunAddress\":\"서울특별시 중구 명동1가 48-2\",\"englishAddress\":\"8, Myeongdong 7-gil, Jung-gu, Seoul, Republic of Korea\",\"addressElements\":[{\"types\":[\"SIDO\"],\"longName\":\"서울특별시\",\"shortName\":\"서울특별시\",\"code\":\"\"},{\"types\":[\"SIGUGUN\"],\"longName\":\"중구\",\"shortName\":\"중구\",\"code\":\"\"},{\"types\":[\"DONGMYUN\"],\"longName\":\"명동1가\",\"shortName\":\"명동1가\",\"code\":\"\"},{\"types\":[\"RI\"],\"longName\":\"\",\"shortName\":\"\",\"code\":\"\"},{\"types\":[\"ROAD_NAME\"],\"longName\":\"명동7길\",\"shortName\":\"명동7길\",\"code\":\"\"},{\"types\":[\"BUILDING_NUMBER\"],\"longName\":\"8\",\"shortName\":\"8\",\"code\":\"\"},{\"types\":[\"BUILDING_NAME\"],\"longName\":\"\",\"shortName\":\"\",\"code\":\"\"},{\"types\":[\"LAND_NUMBER\"],\"longName\":\"48-2\",\"shortName\":\"48-2\",\"code\":\"\"},{\"types\":[\"POSTAL_CODE\"],\"longName\":\"04534\",\"shortName\":\"04534\",\"code\":\"\"}],\"x\":\"126.9847333\",\"y\":\"37.5642362\",\"distance\":0.0},{\"roadAddress\":\"서울특별시 중구 명동7길 12\",\"jibunAddress\":\"서울특별시 중구 명동1가 47-1\",\"englishAddress\":\"12, Myeongdong 7-gil, Jung-gu, Seoul, Republic of Korea\",\"addressElements\":[{\"types\":[\"SIDO\"],\"longName\":\"서울특별시\",\"shortName\":\"서울특별시\",\"code\":\"\"},{\"types\":[\"SIGUGUN\"],\"longName\":\"중구\",\"shortName\":\"중구\",\"code\":\"\"},{\"types\":[\"DONGMYUN\"],\"longName\":\"명동1가\",\"shortName\":\"명동1가\",\"code\":\"\"},{\"types\":[\"RI\"],\"longName\":\"\",\"shortName\":\"\",\"code\":\"\"},{\"types\":[\"ROAD_NAME\"],\"longName\":\"명동7길\",\"shortName\":\"명동7길\",\"code\":\"\"},{\"types\":[\"BUILDING_NUMBER\"],\"longName\":\"12\",\"shortName\":\"12\",\"code\":\"\"},{\"types\":[\"BUILDING_NAME\"],\"longName\":\"\",\"shortName\":\"\",\"code\":\"\"},{\"types\":[\"LAND_NUMBER\"],\"longName\":\"47-1\",\"shortName\":\"47-1\",\"code\":\"\"},{\"types\":[\"POSTAL_CODE\"],\"longName\":\"04534\",\"shortName\":\"04534\",\"code\":\"\"}],\"x\":\"126.9845564\",\"y\":\"37.5643194\",\"distance\":0.0}],\"errorMessage\":\"\"}";
        Mockito.when(geoCodingClient.request(address))
                .thenAnswer(invocation -> response);

        // when
        Optional<GeoLocation> geoLocation = geoCodingService.getGeoLocation(address);

        // then
        assertThat(geoLocation.isPresent()).isTrue();
        assertThat(geoLocation.get().getLatitude()).isEqualTo(37.5642362);
        assertThat(geoLocation.get().getLongitude()).isEqualTo(126.9847333);
    }
}
