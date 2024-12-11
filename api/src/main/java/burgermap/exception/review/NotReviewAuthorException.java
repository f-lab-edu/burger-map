package burgermap.exception.review;

public class NotReviewAuthorException extends RuntimeException {
    public NotReviewAuthorException(Long reviewId, Long memberId) {
        super("Unauthorized: Member with ID %d is not the author of Review with ID %d."
                .formatted(memberId, reviewId));
    }
}
