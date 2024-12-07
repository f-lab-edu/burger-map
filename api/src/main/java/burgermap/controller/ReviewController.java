package burgermap.controller;

import burgermap.annotation.CheckLogin;
import burgermap.dto.review.ReviewAddRequestDto;
import burgermap.dto.review.ReviewResponseDto;
import burgermap.entity.Review;
import burgermap.service.ReviewService;
import burgermap.session.SessionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 추가
    @CheckLogin
    @PostMapping("foods/{foodId}/reviews")
    public ResponseEntity<ReviewResponseDto> addReview(
            @RequestBody ReviewAddRequestDto reviewAddRequestDto,
            @SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID) Long memberId,
            @PathVariable Long foodId) {
        Review review = cvtToReview(reviewAddRequestDto);
        reviewService.addReview(review, memberId, foodId);
        return ResponseEntity.ok(cvtToReviewResponseDto(review));
    }

    // 개별 리뷰 조회
    @GetMapping("reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview (@PathVariable Long reviewId) {
        Review review = reviewService.getReview(reviewId);
        return ResponseEntity.ok(cvtToReviewResponseDto(review));
    }

    // 로그인한 회원이 작성한 모든 리뷰 조회
    @CheckLogin
    @GetMapping("reviews/my-reviews")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews (
            @SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID) Long memberId) {
        return ResponseEntity.ok(cvtToReviewResponseDtoList(reviewService.getMemberReviews(memberId)));
    }

    // 특정 음식에 대한 모든 리뷰 조회
    @GetMapping("foods/{foodId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getFoodReviews (@PathVariable Long foodId) {
        return ResponseEntity.ok(cvtToReviewResponseDtoList(reviewService.getFoodReviews(foodId)));
    }

    // 특정 가게에 대한 모든 리뷰 조회
    @GetMapping("stores/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getStoreReviews (@PathVariable Long storeId) {
        return ResponseEntity.ok(cvtToReviewResponseDtoList(reviewService.getStoreReviews(storeId)));
    }

    @DeleteMapping("reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> deleteReview(
            @SessionAttribute(name = SessionConstants.loginMember) Long memberId,
            @PathVariable Long reviewId) {
        Review review = reviewService.deleteReview(reviewId, memberId);
        return ResponseEntity.ok(cvtToReviewResponseDto(review));
    }

    // DTO -> Entity
    public Review cvtToReview(ReviewAddRequestDto reviewAddRequestDto) {
        Review review = new Review();
        review.setContent(reviewAddRequestDto.getContent());
        log.debug("review info: {}", review);
        return review;
    }

    // Entity -> DTO
    public ReviewResponseDto cvtToReviewResponseDto(Review review) {
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        reviewResponseDto.setReviewId(review.getReviewId());
        reviewResponseDto.setContent(review.getContent());
        reviewResponseDto.setMemberNickname(review.getMember().getNickname());
        reviewResponseDto.setStoreId(review.getStore().getStoreId());
        reviewResponseDto.setStoreName(review.getStore().getName());
        reviewResponseDto.setFoodId(review.getFood().getFoodId());
        reviewResponseDto.setFoodName(review.getFood().getName());
        return reviewResponseDto;
    }

    // List<Entity> -> List<DTO>
    public List<ReviewResponseDto> cvtToReviewResponseDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(this::cvtToReviewResponseDto)
                .toList();
    }
}
