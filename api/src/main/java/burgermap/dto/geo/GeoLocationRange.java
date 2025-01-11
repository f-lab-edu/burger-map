package burgermap.dto.geo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GeoLocationRange {
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;
}
