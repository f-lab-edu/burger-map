package burgermap.service;

import burgermap.entity.Food;
import burgermap.entity.Member;
import burgermap.entity.Review;
import burgermap.exception.food.FoodNotExistException;
import burgermap.exception.member.MemberNotExistException;
import burgermap.exception.review.NotReviewAuthorException;
import burgermap.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;

    private final ReviewLookupService reviewLookupService;
    private final MemberLookupService memberLookupService;
    private final FoodLookupService foodLookupService;

    @Transactional
    public void addReview(Review review, Long memberId, Long foodId) {
        // TODO: 회원이 CUSTOMER 타입인지 확인, 아닌 경우 예외
        CompletableFuture<Optional<Food>> foodAsync = foodLookupService.findByFoodIdAsync(foodId);
        CompletableFuture<Optional<Member>> memberAsync = memberLookupService.findByMemberIdAsync(memberId);

        CompletableFuture.allOf(foodAsync, memberAsync).join();

        Food food = foodAsync.join().orElseThrow(() -> new FoodNotExistException(foodId));
        Member member = memberAsync.join().orElseThrow(() -> new MemberNotExistException(memberId));

        review.setFood(food);
        review.setStore(food.getStore());
        review.setMember(member);
        repository.save(review);
        log.debug("review added: {}", review);
    }

    public Review getReview(Long reviewId) {
        return reviewLookupService.findByReviewId(reviewId);
    }

    @Transactional
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
