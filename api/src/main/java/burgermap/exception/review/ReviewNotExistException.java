package burgermap.exception.review;

public class ReviewNotExistException extends RuntimeException {
    public ReviewNotExistException(Long reviewId) {
        super("Review with review ID %d does not exist.".formatted(reviewId));
    }
}
