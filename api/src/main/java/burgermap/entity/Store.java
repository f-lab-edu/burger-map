package burgermap.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    private Long storeId;  // id
//    private Long memberId;
    private Member member;
    private String name;
    private String address;
    private String phone;
    private String introduction;
}
