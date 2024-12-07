package burgermap.repository;

import burgermap.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataJpaReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.store.storeId = :storeId")
    List<Review> findByStoreId(@Param("storeId") Long storeId);
    @Query("select r from Review r where r.member.memberId = :memberId")
    List<Review> findByMemberId(@Param("memberId") Long memberId);
    @Query("select r from Review r where r.food.foodId = :foodId")
    List<Review> findByFoodId(@Param("foodId") Long foodId);
}
