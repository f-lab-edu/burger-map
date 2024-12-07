package burgermap.repository;

import burgermap.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    public Review save(Review review);
    public Optional<Review> findByReviewId(Long reviewId);
    public Optional<Review> deleteByReviewId(Long reviewId);
    public List<Review> findByStoreId(Long storeId);
    public List<Review> findByMemberId(Long memberId);
    public List<Review> findByFoodId(Long foodId);
}
