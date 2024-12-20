package burgermap.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;
    @ManyToOne
    private Member member;
    private String name;
    private String address;
    private String phone;
    private String introduction;
    private double latitude;
    private double longitude;

    @Builder
    public Store(String name, String address, String phone, String introduction) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.introduction = introduction;
    }
}
