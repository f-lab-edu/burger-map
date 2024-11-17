package burgermap.service;

import burgermap.entity.Food;
import burgermap.entity.Review;
import burgermap.exception.review.NotReviewAuthorException;
import burgermap.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;

    private final ReviewLookupService reviewLookupService;
    private final MemberLookupService memberLookupService;
    private final StoreLookupService storeLookupService;
    private final FoodLookupService foodLookupService;

    public void addReview(Review review, Long memberId, Long foodId) {
        // TODO: 회원이 CUSTOMER 타입인지 확인, 아닌 경우 예외
        Food food = foodLookupService.findByFoodId(foodId);
        review.setFood(food);
        review.setStore(food.getStore());
        review.setMember(memberLookupService.findByMemberId(memberId));
        repository.save(review);
        log.debug("review added: {}", review);
    }

    public Review getReview(Long reviewId) {
        return reviewLookupService.findByReviewId(reviewId);
    }

    public Review deleteReview(Long reviewId, Long memberId) {
        Review review = reviewLookupService.findByReviewId(reviewId);

        // 리뷰 작성 회원과 삭제 요청 회원이 같은지 확인, 아닌 경우 예외 발생
        if (!review.getMember().getMemberId().equals(memberId)) {
            throw new NotReviewAuthorException(reviewId, memberId);
        }

        repository.deleteByReviewId(reviewId);
        log.debug("review deleted: {}", review);
        return review;
    }

    public List<Review> getStoreReviews(Long storeId) {
        return repository.findByStoreId(storeId);
    }

    public List<Review> getMemberReviews(Long memberId) {
        return repository.findByMemberId(memberId);
    }

    public List<Review> getFoodReviews(Long foodId) {
        return repository.findByFoodId(foodId);
    }
}
