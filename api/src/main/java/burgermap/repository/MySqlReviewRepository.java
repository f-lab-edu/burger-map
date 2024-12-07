package burgermap.repository;

import burgermap.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MySqlReviewRepository implements ReviewRepository {

    private final SpringDataJpaReviewRepository repository;

    @Override
    public Review save(Review review) {
        repository.save(review);
        return review;
    }

    @Override
    public Optional<Review> findByReviewId(Long reviewId) {
        return repository.findById(reviewId);
    }

    @Override
    public Optional<Review> deleteByReviewId(Long reviewId) {
        return repository.findById(reviewId)
                .map(review -> {
                    repository.delete(review);
                    return review;
                });
    }

    @Override
    public List<Review> findByStoreId(Long storeId) {
        return repository.findByStoreId(storeId);
    }

    @Override
    public List<Review> findByMemberId(Long memberId) {
        return repository.findByMemberId(memberId);
    }

    @Override
    public List<Review> findByFoodId(Long foodId) {
        return repository.findByFoodId(foodId);
    }
}
