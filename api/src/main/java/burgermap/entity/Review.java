package burgermap.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Review {
    @Id
    private Long reviewId;
    @ManyToOne
    private Member member;
    @ManyToOne
    private Store store;
    @ManyToOne
    private Food food;
    private String content;
}
