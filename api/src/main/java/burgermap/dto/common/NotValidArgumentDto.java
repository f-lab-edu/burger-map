package burgermap.dto.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotValidArgumentDto {
    private String objectError;
    private String fieldName;
    private String message;
}
